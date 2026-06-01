# Inventory Service

Manages stock levels by consuming product events.

## Responsibilities

- Inventory management
- Consume ProductCreated, ProductUpdated, ProductDeleted events
- Maintain stock information

## Tech

- Java 21, Spring Boot 3
- PostgreSQL (inventory_db)
- RabbitMQ (event consumer)

## Port

`8081`
