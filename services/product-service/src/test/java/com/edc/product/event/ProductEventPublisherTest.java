package com.edc.product.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.edc.product.config.RabbitMQConfig;
import com.edc.product.product.Product;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class ProductEventPublisherTest {

  @Mock
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private ProductEventPublisher publisher;

  @Test
  void shouldPublishCreatedEvent() {
    var product = new Product("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);

    publisher.publishCreated(product);

    verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EXCHANGE), eq("product.created"), any(ProductEvent.class));
  }

  @Test
  void shouldPublishUpdatedEvent() {
    var product = new Product("Mouse", "Wireless", BigDecimal.valueOf(29.99), 100);

    publisher.publishUpdated(product);

    verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EXCHANGE), eq("product.updated"), any(ProductEvent.class));
  }

  @Test
  void shouldPublishDeletedEvent() {
    var productId = UUID.randomUUID();

    publisher.publishDeleted(productId);

    verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EXCHANGE), eq("product.deleted"), any(ProductEvent.class));
  }
}
