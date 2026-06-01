package com.edc.product.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE = "product.exchange";

  private final RabbitTemplate rabbitTemplate;

  public RabbitMQConfig(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @PostConstruct
  void configureMessageConverter() {
    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
  }

  @Bean
  TopicExchange productExchange() {
    return new TopicExchange(EXCHANGE, true, false);
  }
}
