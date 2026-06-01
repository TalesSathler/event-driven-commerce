# System Architecture

## Overview

The platform follows a microservices architecture with service-per-database isolation. Communication between services is done via REST (synchronous) and RabbitMQ (asynchronous). All traffic flows through an API Gateway that routes to downstream services.

## Architecture Diagram

```
                          +-------------------+
                          |   Angular SPA     |
                          |   localhost:4200   |
                          +---------+---------+
                                    |
                                    | HTTP (REST)
                                    v
                          +-------------------+
                          |   API Gateway     |
                          |   localhost:8080   |
                          +-------+-------+---+
                                  |       |
                    +-------------+  +----+-----------+
                    |                |                |
                    v                v                v
          +-----------------+  +-----------+  +-----------------+
          |   Auth Service  |  |  Product  |  | Inventory       |
          |   localhost:8081|  |  Service  |  | Service         |
          +--------+--------+  | :8082     |  | :8083           |
                   |           +-----+-----+  +--------+--------+
                   | RabbitMQ        |                  |
                   |                 | RabbitMQ          |
                   v                 v                  v
          +------------------+  +------------+  +------------------+
          |    PostgreSQL    |  |  RabbitMQ  |  |    PostgreSQL    |
          |    auth_db :5432 |  | topic ex.  |  | inventory_db    |
          +------------------+  +-----+------+  | :5434           |
                                        |       +------------------+
                                        | product.created
                                        | product.updated
                                        | product.deleted
                                        v
                               +-------------------+
                               | Inventory Service |
                               | (Event Consumer)  |
                               +-------------------+
```

## Authentication Flow

```
1. User → Auth Service: POST /api/auth/login (email + password)
2. Auth Service → User: JWT (RS256-signed) + Refresh Token
3. User → API Gateway: HTTP request + Authorization: Bearer <JWT>
4. Gateway → Product/Inventory Service: forwards request + JWT
5. Service: validates JWT locally using RS256 public key
6. Service: authorizes based on roles in JWT claims
7. Service → User: response

Same flow for Inventory Service — no runtime calls to Auth Service.
```

## Services

| Service            | Port | Database       | Purpose                                      |
|--------------------|------|----------------|----------------------------------------------|
| API Gateway        | 8080 | —              | Reverse proxy, route to downstream services  |
| Auth Service       | 8081 | auth_db        | User registration, login, JWT issuance       |
| Product Service    | 8082 | product_db     | Product CRUD, Event publishing               |
| Inventory Service  | 8083 | inventory_db   | Stock tracking, Event consumption            |
| RabbitMQ           | 5672 | —              | Message broker (topic exchange)              |
| PostgreSQL Auth    | 5432 | auth_db        | Auth service data (users, refresh tokens)    |
| PostgreSQL Product | 5433 | product_db     | Product service data                         |
| PostgreSQL Inv.    | 5434 | inventory_db   | Inventory service data                       |
| Angular Frontend   | 4200 | —              | SPA user interface                           |

## Communication Patterns

### Synchronous (REST via API Gateway)
- Angular ↔ API Gateway → Auth Service: Login, register, refresh
- Angular ↔ API Gateway → Product Service: Product CRUD
- Angular ↔ API Gateway → Inventory Service: Stock queries, adjustments

### Asynchronous (RabbitMQ)
- Product Service → Inventory Service: Product lifecycle events
- Exchange type: `topic`
- Exchange name: `product.exchange`

## Trust Boundaries

- **Auth Service**: Trusted — handles credentials, holds private key
- **Product Service**: Untrusted for auth — validates JWTs locally with public key only
- **Inventory Service**: Untrusted for auth — validates JWTs locally with public key only
- **API Gateway**: Transparent proxy — does not inspect JWT payloads
- **RabbitMQ**: Internal network — events trusted within cluster

## Infrastructure

All services run in Docker Compose. The Angular frontend uses a volume mount for hot-reload during development. PostgreSQL instances use separate ports to avoid conflicts on the host.
