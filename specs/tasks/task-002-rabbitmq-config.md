# Task: Configure RabbitMQ Exchange and Queues

Status: DONE

Dependencies: none

## Description

Configure RabbitMQ infrastructure in the Inventory Service: declare the `product.exchange` topic exchange and the three queues (`inventory.product.created`, `inventory.product.updated`, `inventory.product.deleted`) with their routing key bindings.

Refer to specs/events/product-events.md for exchange/queue/routing key definitions.

Use Spring AMQP's `@Bean` declarations in a `RabbitMQConfig` class.

## Acceptance Criteria

- [ ] `RabbitMQConfig` class exists in `com.edc.inventory.config`
- [ ] Exchange `product.exchange` is declared as topic type, durable
- [ ] Queue `inventory.product.created` is declared with binding to `product.created`
- [ ] Queue `inventory.product.updated` is declared with binding to `product.updated`
- [ ] Queue `inventory.product.deleted` is declared with binding to `product.deleted`
- [ ] All declarations happen on application startup
- [ ] Queues and exchange are visible in RabbitMQ Management UI on startup
