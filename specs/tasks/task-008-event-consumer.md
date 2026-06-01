# Task: Implement Product Event Consumer

Status: DONE

Dependencies: task-006-inventory-entity, task-002-rabbitmq-config

## Description

Implement the RabbitMQ consumer in the Inventory Service that listens to `product.created`, `product.updated`, and `product.deleted` events. The consumer must create/update/delete inventory records accordingly.

Refer to specs/events/product-events.md for consumer behavior and specs/services/inventory-service.md for business rules.

## Acceptance Criteria

- [ ] `ProductEventConsumer.java` exists in `com.edc.inventory.event` package
- [ ] `@RabbitListener` annotated methods for each queue
- [ ] PRODUCT_CREATED creates a new inventory record with the product's initial quantity (upsert for idempotency)
- [ ] PRODUCT_UPDATED updates the product name on the inventory record (quantity NOT synced)
- [ ] PRODUCT_DELETED removes the inventory record
- [ ] Events with duplicate eventId are handled idempotently
- [ ] Error handling: consumer does not crash on invalid payloads
- [ ] Integration: events published from Product Service appear in inventory database
