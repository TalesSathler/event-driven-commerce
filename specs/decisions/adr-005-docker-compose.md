# ADR-005: Docker Compose for Local Development

## Status

Accepted

## Decision

Use Docker Compose to run the entire platform locally, with volume mounts for Angular hot-reload.

## Context

The platform consists of 6 containers (2 PostgreSQL, RabbitMQ, 2 Spring Boot services, Angular frontend). Developers need a simple way to start everything, see logs, and rebuild individual services.

## Options Considered

1. **Docker Compose** — Single command to start everything, easy rebuild, log aggregation.
2. **Manual startup** — Start each service separately. Too many commands, error-prone.
3. **Kubernetes (Minikube)** — Over-engineered for local development.
4. **Podman Compose** — Docker Compose alternative. Less widely used.

## Rationale

- Docker Compose is the simplest solution for multi-container local development
- Single `docker compose up -d` starts all 6 containers
- Volume mounts enable Angular hot-reload without rebuilding the frontend container
- `docker compose logs <service>` provides per-service log access
- Each service can be rebuilt independently with `docker compose build <service>`
- All ports are explicitly mapped to avoid conflicts

## Consequences

- Docker must be installed on the development machine
- Port conflicts if other services use the same ports (5432, 5433, 5672, 8080, 8081, 4200)
- Volume mounts require the frontend source to exist on the host
- `CHOKIDAR_USEPOLLING=1` is needed for file watching inside Docker containers

## Related

- specs/architecture/deployment.md
- docker-compose.yml
