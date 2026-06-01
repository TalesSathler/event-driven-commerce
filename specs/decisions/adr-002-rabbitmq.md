# ADR-002: RabbitMQ for Async Messaging

## Status

Accepted

## Decision

Use RabbitMQ as the message broker for asynchronous communication between services.

## Context

Service A (Product Service) needs to notify Service B (Inventory Service) when products are created, updated, or deleted. This must be done asynchronously to avoid tight coupling.

## Options Considered

1. **RabbitMQ** — Mature, excellent Spring Boot integration (Spring AMQP), supports topic exchanges, retries, DLQ.
2. **Apache Kafka** — Better for high-throughput event streaming. Overkill for current project scale.
3. **HTTP callbacks** — Tight coupling, no retry guarantees, no broker resilience.
4. **Redis Pub/Sub** — No message persistence, no delivery guarantees.

## Rationale

- RabbitMQ has first-class Spring Boot support via Spring AMQP
- Topic exchanges provide flexible routing without the consumer knowing producer details
- Built-in message persistence and delivery acknowledgment
- DLQ and retry support for reliability patterns in future sprints
- Widely used in enterprise Java ecosystems

## Consequences

- Additional infrastructure (RabbitMQ container in Docker Compose)
- Learning curve for exchange/queue/binding configuration
- Event contract versioning becomes important
- Enables adding new consumers without changing producers

## Related

- docs/rabbitmq-flow.md
