package com.edc.product.event;

import com.edc.product.config.RabbitMQConfig;
import com.edc.product.product.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

  private static final Logger log = LoggerFactory.getLogger(ProductEventPublisher.class);

  private final RabbitTemplate rabbitTemplate;

  public ProductEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishCreated(Product product) {
    var event = buildEvent(product);
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "product.created", event);
    log.info("Published PRODUCT_CREATED for product: {}", product.getId());
  }

  public void publishUpdated(Product product) {
    var event = buildEvent(product);
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "product.updated", event);
    log.info("Published PRODUCT_UPDATED for product: {}", product.getId());
  }

  public void publishDeleted(UUID productId) {
    var event = new ProductEvent(UUID.randomUUID(), productId, null, null, null, LocalDateTime.now());
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "product.deleted", event);
    log.info("Published PRODUCT_DELETED for product: {}", productId);
  }

  private ProductEvent buildEvent(Product product) {
    return new ProductEvent(
        UUID.randomUUID(),
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getQuantity(),
        LocalDateTime.now()
    );
  }
}
