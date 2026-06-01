package com.edc.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE = "product.exchange";
  public static final String QUEUE_CREATED = "inventory.product.created";
  public static final String QUEUE_UPDATED = "inventory.product.updated";
  public static final String QUEUE_DELETED = "inventory.product.deleted";
  public static final String ROUTING_CREATED = "product.created";
  public static final String ROUTING_UPDATED = "product.updated";
  public static final String ROUTING_DELETED = "product.deleted";

  @Bean
  TopicExchange productExchange() {
    return new TopicExchange(EXCHANGE, true, false);
  }

  @Bean
  Queue inventoryProductCreatedQueue() {
    return new Queue(QUEUE_CREATED, true);
  }

  @Bean
  Queue inventoryProductUpdatedQueue() {
    return new Queue(QUEUE_UPDATED, true);
  }

  @Bean
  Queue inventoryProductDeletedQueue() {
    return new Queue(QUEUE_DELETED, true);
  }

  @Bean
  Binding bindCreated() {
    return BindingBuilder.bind(inventoryProductCreatedQueue())
        .to(productExchange())
        .with(ROUTING_CREATED);
  }

  @Bean
  Binding bindUpdated() {
    return BindingBuilder.bind(inventoryProductUpdatedQueue())
        .to(productExchange())
        .with(ROUTING_UPDATED);
  }

  @Bean
  Binding bindDeleted() {
    return BindingBuilder.bind(inventoryProductDeletedQueue())
        .to(productExchange())
        .with(ROUTING_DELETED);
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
