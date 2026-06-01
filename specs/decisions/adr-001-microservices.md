# ADR-001: Microservices Architecture

## Status

Accepted

## Decision

Use a microservices architecture with two initial services (Product Service, Inventory Service) communicating via REST and RabbitMQ.

## Context

The project needs to demonstrate event-driven architecture in a realistic distributed system. A monolithic approach would not achieve the learning goals of inter-service communication, event-driven patterns, and service isolation.

## Options Considered

1. **Monolith** — Single deployable unit. Simpler but does not achieve learning goals.
2. **Microservices with REST only** — Services communicate only via HTTP. Works but lacks async event-driven pattern.
3. **Microservices with RabbitMQ** — Adds async messaging. More complex but demonstrates loose coupling and event-driven patterns.

## Rationale

- Microservices with RabbitMQ best achieves the learning goals of the project
- Each service can be developed, deployed, and scaled independently
- RabbitMQ enables async communication, which is the core learning objective
- Two services is a minimal setup to demonstrate the pattern without over-engineering

## Consequences

- Increased operational complexity (multiple databases, message broker)
- Requires event contract management
- Enables loose coupling and independent service evolution
- Establishes a pattern that scales to additional services (Order, Payment, Notification)

## Related

- docs/architecture.md
