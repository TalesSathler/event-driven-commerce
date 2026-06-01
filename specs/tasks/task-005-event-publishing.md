# Task: Implement RabbitMQ Event Publishing

Status: DONE

Dependencies: task-004-product-crud, task-002-rabbitmq-config

## Description

Publish RabbitMQ events from the Product Service when products are created, updated, or deleted. Use `RabbitTemplate` to publish to the `product.exchange` with the appropriate routing keys.

Refer to specs/events/product-events.md for payload structures and routing keys.

## Acceptance Criteria

- [ ] `RabbitMQConfig.java` or equivalent declares the `product.exchange` topic exchange in Product Service
- [ ] `ProductEventPublisher.java` (or similar) exists in `com.edc.product.event` package
- [ ] PRODUCT_CREATED is published with routing key `product.created` after product creation
- [ ] PRODUCT_UPDATED is published with routing key `product.updated` after product update
- [ ] PRODUCT_DELETED is published with routing key `product.deleted` after product deletion
- [ ] Event payload includes: eventId (UUID), productId, name, price, quantity, timestamp
- [ ] Events are published after successful database write, not before
- [ ] Events are visible in RabbitMQ Management UI
