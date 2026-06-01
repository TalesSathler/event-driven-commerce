package com.edc.inventory.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import com.edc.inventory.inventory.InventoryRepository;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
    "spring.rabbitmq.host=localhost",
    "spring.rabbitmq.port=5672"
})
class RabbitMQConfigTest {

  @MockBean
  private ConnectionFactory connectionFactory;

  @MockBean
  private InventoryRepository inventoryRepository;

  @Autowired
  private TopicExchange productExchange;

  @Autowired
  private Queue inventoryProductCreatedQueue;

  @Autowired
  private Queue inventoryProductUpdatedQueue;

  @Autowired
  private Queue inventoryProductDeletedQueue;

  @Autowired
  private Binding bindCreated;

  @Autowired
  private Binding bindUpdated;

  @Autowired
  private Binding bindDeleted;

  @Test
  void shouldDeclareExchange() {
    assertThat(productExchange.getName()).isEqualTo("product.exchange");
    assertThat(productExchange.isDurable()).isTrue();
  }

  @Test
  void shouldDeclareQueues() {
    assertThat(inventoryProductCreatedQueue.getName()).isEqualTo("inventory.product.created");
    assertThat(inventoryProductUpdatedQueue.getName()).isEqualTo("inventory.product.updated");
    assertThat(inventoryProductDeletedQueue.getName()).isEqualTo("inventory.product.deleted");
  }

  @Test
  void shouldBindCreatedQueue() {
    assertThat(bindCreated.getDestination()).isEqualTo("inventory.product.created");
    assertThat(bindCreated.getRoutingKey()).isEqualTo("product.created");
  }

  @Test
  void shouldBindUpdatedQueue() {
    assertThat(bindUpdated.getDestination()).isEqualTo("inventory.product.updated");
    assertThat(bindUpdated.getRoutingKey()).isEqualTo("product.updated");
  }

  @Test
  void shouldBindDeletedQueue() {
    assertThat(bindDeleted.getDestination()).isEqualTo("inventory.product.deleted");
    assertThat(bindDeleted.getRoutingKey()).isEqualTo("product.deleted");
  }
}
