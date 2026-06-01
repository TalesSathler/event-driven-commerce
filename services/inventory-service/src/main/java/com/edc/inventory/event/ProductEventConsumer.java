package com.edc.inventory.event;

import com.edc.inventory.config.RabbitMQConfig;
import com.edc.inventory.inventory.Inventory;
import com.edc.inventory.inventory.InventoryRepository;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductEventConsumer {

  private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

  private final InventoryRepository inventoryRepository;
  private final Set<UUID> processedEvents = ConcurrentHashMap.newKeySet();

  public ProductEventConsumer(InventoryRepository inventoryRepository) {
    this.inventoryRepository = inventoryRepository;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_CREATED)
  @Transactional
  public void handleProductCreated(ProductEvent event) {
    if (event == null || event.eventId() == null) {
      log.warn("Received invalid PRODUCT_CREATED event");
      return;
    }
    if (!processedEvents.add(event.eventId())) {
      log.info("Duplicate PRODUCT_CREATED event ignored: {}", event.eventId());
      return;
    }
    try {
      var existing = inventoryRepository.findByProductId(event.productId());
      if (existing.isPresent()) {
        var inventory = existing.get();
        inventory.setProductName(event.name());
        inventoryRepository.save(inventory);
        log.info("Updated existing inventory for product: {}", event.productId());
      } else {
        var quantity = event.quantity() != null ? event.quantity() : 0;
        var inventory = new Inventory(event.productId(), event.name(), quantity);
        inventoryRepository.save(inventory);
        log.info("Created inventory for product: {}", event.productId());
      }
    } catch (DataIntegrityViolationException e) {
      log.warn("Duplicate product event, inventory already exists for: {}", event.productId());
    }
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_UPDATED)
  @Transactional
  public void handleProductUpdated(ProductEvent event) {
    if (event == null || event.eventId() == null) {
      log.warn("Received invalid PRODUCT_UPDATED event");
      return;
    }
    if (!processedEvents.add(event.eventId())) {
      log.info("Duplicate PRODUCT_UPDATED event ignored: {}", event.eventId());
      return;
    }
    var existing = inventoryRepository.findByProductId(event.productId());
    if (existing.isPresent()) {
      var inventory = existing.get();
      inventory.setProductName(event.name());
      inventoryRepository.save(inventory);
      log.info("Updated inventory name for product: {}", event.productId());
    } else {
      log.warn("Received update for non-existent inventory: {}", event.productId());
    }
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_DELETED)
  @Transactional
  public void handleProductDeleted(ProductEvent event) {
    if (event == null || event.eventId() == null) {
      log.warn("Received invalid PRODUCT_DELETED event");
      return;
    }
    if (!processedEvents.add(event.eventId())) {
      log.info("Duplicate PRODUCT_DELETED event ignored: {}", event.eventId());
      return;
    }
    inventoryRepository.findByProductId(event.productId()).ifPresent(inventory -> {
      inventoryRepository.delete(inventory);
      log.info("Deleted inventory for product: {}", event.productId());
    });
  }
}
