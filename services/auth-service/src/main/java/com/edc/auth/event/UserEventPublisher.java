package com.edc.auth.event;

import com.edc.auth.config.RabbitMQConfig;
import com.edc.auth.user.User;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

  private static final Logger log = LoggerFactory.getLogger(UserEventPublisher.class);

  private final RabbitTemplate rabbitTemplate;

  public UserEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishRegistered(User user) {
    var event = buildEvent("USER_REGISTERED", user);
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "user.registered", event);
    log.info("Published USER_REGISTERED for user: {}", user.getId());
  }

  public void publishUpdated(User user) {
    var event = buildEvent("USER_UPDATED", user);
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "user.updated", event);
    log.info("Published USER_UPDATED for user: {}", user.getId());
  }

  public void publishDeleted(UUID userId, String email, String role) {
    var event = new UserEvent(UUID.randomUUID(), "USER_DELETED", userId, email, role, LocalDateTime.now());
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "user.deleted", event);
    log.info("Published USER_DELETED for user: {}", userId);
  }

  public void publishLogin(User user) {
    var event = buildEvent("USER_LOGIN", user);
    rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "user.login", event);
    log.info("Published USER_LOGIN for user: {}", user.getId());
  }

  private UserEvent buildEvent(String eventType, User user) {
    return new UserEvent(
        UUID.randomUUID(),
        eventType,
        user.getId(),
        user.getEmail(),
        user.getRole(),
        LocalDateTime.now()
    );
  }
}
