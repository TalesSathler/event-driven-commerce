# Deployment

## Local Development

The entire platform runs locally via Docker Compose. All services are defined in a single `docker-compose.yml` at the project root.

### Startup

```bash
docker compose up -d
```

This starts all 6 containers:
- edc-postgres-product (port 5432)
- edc-postgres-inventory (port 5433)
- edc-rabbitmq (port 5672, management UI on 15672)
- edc-product-service (port 8080)
- edc-inventory-service (port 8081)
- edc-frontend (port 4200)

### Hot Reload

The Angular frontend mounts the source directory as a Docker volume, enabling hot-reload. Changes to frontend source files are reflected immediately without rebuilding the container.

### Rebuilding a Service

```bash
docker compose build <service-name>
docker compose up -d <service-name>
```

## Container Architecture

| Container             | Image                          | Exposed Ports |
|-----------------------|--------------------------------|---------------|
| edc-postgres-product  | postgres:16-alpine              | 5432          |
| edc-postgres-inventory| postgres:16-alpine              | 5433          |
| edc-rabbitmq          | rabbitmq:3-management-alpine    | 5672, 15672   |
| edc-product-service   | event-driven-commerce-product-service | 8080      |
| edc-inventory-service | event-driven-commerce-inventory-service | 8081  |
| edc-frontend          | event-driven-commerce-frontend    | 4200          |

## Database Initialization

- Product Service uses Flyway for schema migrations (`V1__`, `V2__`, etc.)
- Inventory Service uses Flyway for schema migrations
- Hibernate `ddl-auto: validate` ensures entities match the Flyway-managed schema
- PostgreSQL credentials default to `postgres` / `postgres`

## RabbitMQ Management

RabbitMQ Management UI is available at `http://localhost:15672`
- Username: `admin`
- Password: `admin`
