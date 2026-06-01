# Docker Infrastructure

This directory contains configuration files for infrastructure services used by the platform.

## Services

| Directory | Service | Purpose |
|-----------|---------|---------|
| `postgres/` | PostgreSQL 16 | Product and inventory databases |
| `rabbitmq/` | RabbitMQ 3 | Event broker for async communication |

## Usage

Infrastructure is started via `docker compose up -d` from the project root.

Each service directory contains:
- Configuration files mounted into the container
- Initialization scripts run on first start
