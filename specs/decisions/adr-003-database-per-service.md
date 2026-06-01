# ADR-003: Database per Service

## Status

Accepted

## Decision

Each microservice owns its own PostgreSQL database instance. No direct database sharing between services.

## Context

A core principle of microservices is loose coupling. Sharing a database between services creates tight coupling at the data layer and makes it difficult to evolve schemas independently.

## Options Considered

1. **Single shared database** — One PostgreSQL instance for all services. Easier initial setup but tightly couples services.
2. **Database per service** — Each service has its own PostgreSQL instance. True service isolation.
3. **Mixed** — Some services share databases. Arbitrary and inconsistent.

## Rationale

- Database per service is a core microservices pattern
- Services can change their schema without affecting other services
- No risk of one service locking tables needed by another
- Scales naturally as new services are added
- Each service can choose the optimal database type for its needs

## Consequences

- More infrastructure to manage (one PostgreSQL instance per service)
- Each PostgreSQL instance runs on a separate port to avoid host conflicts
- Cross-service queries require API calls or events (no SQL JOINs across services)
- Data duplication may occur (e.g., product name stored in both product_db and inventory_db)

## Related

- docs/architecture.md (Database Strategy section)
- specs/database/database-specs.md
