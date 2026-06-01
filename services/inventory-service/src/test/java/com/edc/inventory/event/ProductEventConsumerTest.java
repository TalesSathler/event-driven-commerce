package com.edc.inventory.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edc.inventory.inventory.Inventory;
import com.edc.inventory.inventory.InventoryRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductEventConsumerTest {

  @Mock
  private InventoryRepository inventoryRepository;

  private ProductEventConsumer consumer;

  @BeforeEach
  void setUp() {
    consumer = new ProductEventConsumer(inventoryRepository);
  }

  @Test
  void shouldCreateInventoryOnProductCreated() {
    var productId = UUID.randomUUID();
    var event = new ProductEvent(UUID.randomUUID(), productId, "Mouse", BigDecimal.valueOf(29.99), 100,
        LocalDateTime.now());

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

    consumer.handleProductCreated(event);

    verify(inventoryRepository).save(any(Inventory.class));
  }

  @Test
  void shouldIgnoreDuplicateProductCreatedEvent() {
    var eventId = UUID.randomUUID();
    var productId = UUID.randomUUID();
    var event = new ProductEvent(eventId, productId, "Mouse", BigDecimal.valueOf(29.99), 100,
        LocalDateTime.now());

    consumer.handleProductCreated(event);
    consumer.handleProductCreated(event);

    verify(inventoryRepository).save(any(Inventory.class));
  }

  @Test
  void shouldUpdateNameOnProductUpdated() {
    var productId = UUID.randomUUID();
    var existing = new Inventory(productId, "Old Name", 100);
    var event = new ProductEvent(UUID.randomUUID(), productId, "New Name", BigDecimal.valueOf(39.99), 50,
        LocalDateTime.now());

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(existing));

    consumer.handleProductUpdated(event);

    verify(inventoryRepository).save(any(Inventory.class));
  }

  @Test
  void shouldNotUpdateQuantityOnProductUpdated() {
    var productId = UUID.randomUUID();
    var existing = new Inventory(productId, "Mouse", 100);
    var event = new ProductEvent(UUID.randomUUID(), productId, "Mouse", BigDecimal.valueOf(39.99), 999,
        LocalDateTime.now());

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(existing));

    consumer.handleProductUpdated(event);

    verify(inventoryRepository).save(any(Inventory.class));
  }

  @Test
  void shouldDeleteInventoryOnProductDeleted() {
    var productId = UUID.randomUUID();
    var existing = new Inventory(productId, "Mouse", 100);
    var event = new ProductEvent(UUID.randomUUID(), productId, null, null, null, LocalDateTime.now());

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(existing));

    consumer.handleProductDeleted(event);

    verify(inventoryRepository).delete(existing);
  }

  @Test
  void shouldNotCrashOnInvalidEvent() {
    consumer.handleProductCreated(null);
    consumer.handleProductUpdated(null);
    consumer.handleProductDeleted(null);
  }

  @Test
  void shouldNotDeleteNonExistentInventory() {
    var productId = UUID.randomUUID();
    var event = new ProductEvent(UUID.randomUUID(), productId, null, null, null, LocalDateTime.now());

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

    consumer.handleProductDeleted(event);

    verify(inventoryRepository, never()).delete(any());
  }

  @Test
  void shouldIgnoreDuplicateProductDeletedEvent() {
    var eventId = UUID.randomUUID();
    var productId = UUID.randomUUID();
    var existing = new Inventory(productId, "Mouse", 100);
    var event = new ProductEvent(eventId, productId, null, null, null, LocalDateTime.now());

    when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(existing));

    consumer.handleProductDeleted(event);
    consumer.handleProductDeleted(event);

    verify(inventoryRepository).delete(existing);
  }
}
