# PostgreSQL

Each microservice gets its own PostgreSQL 16 Alpine instance (Database per Service pattern).

## Instances

| Container | Database | Host Port | Owned By |
|-----------|----------|-----------|----------|
| `edc-postgres-product` | `product_db` | 5432 | Product Service |
| `edc-postgres-inventory` | `inventory_db` | 5433 | Inventory Service |

## Initialization

Each instance creates its database on first start via the `POSTGRES_DB` environment variable. Service-level schema migrations are handled by Flyway in each service.

## Service Discovery

Within the Docker network, services connect to their database by container name:
- Product Service → `postgres-product:5432`
- Inventory Service → `postgres-inventory:5432`
