# Architecture Decisions

## Why This Architecture?

This project is intentionally designed to start simple and evolve incrementally.

The primary goal is to learn:

* Spring Boot Microservices
* RabbitMQ
* Event-Driven Architecture
* Docker
* JWT Authentication (RS256)
* API Gateway pattern

without introducing unnecessary complexity during the first phase.

---

# Current Architecture (v2)

## Services

| Service | Port | Database | Purpose |
|---|---|---|---|
| API Gateway | 8080 | — | Reverse proxy, route to downstream services |
| Auth Service | 8081 | auth_db | User registration, login, JWT issuance (RS256) |
| Product Service | 8082 | product_db | Product CRUD, Event publishing |
| Inventory Service | 8083 | inventory_db | Stock tracking, Event consumption |

## Authentication Flow

```
1. User → Auth Service: POST /api/auth/login (email + password)
2. Auth Service → User: JWT (RS256-signed) + Refresh Token
3. User → API Gateway: HTTP request + Authorization: Bearer <JWT>
4. Gateway → Product/Inventory Service: forwards request + JWT
5. Service: validates JWT locally using RS256 public key
6. Service: authorizes based on roles in JWT claims
7. Service → User: response
```

JWT validation is local — no runtime calls to auth-service.

## Trust Boundaries

- **Auth Service**: Trusted — handles credentials, holds private key
- **Product Service**: Untrusted for auth — validates JWTs locally with public key only
- **Inventory Service**: Untrusted for auth — validates JWTs locally with public key only
- **API Gateway**: Transparent proxy — does not inspect JWT payloads
- **RabbitMQ**: Internal network — events trusted within cluster

---

# Database Strategy

## Database per Service

Each microservice gets its own PostgreSQL instance.

```text
postgres-auth ------> auth_db (Auth Service)
postgres-product ---> product_db (Product Service)
postgres-inventory -> inventory_db (Inventory Service)
```

### Auth Service

Owns:
- Users
- Refresh Tokens

### Product Service

Owns:
- Products

### Inventory Service

Owns:
- Inventory
- Stock Movements

---

## Future Evolution

```text
postgres-order
postgres-payment
postgres-notification
```

Each new service gets its own PostgreSQL instance.

---

# Docker Strategy

## Development Environment

A single Docker Compose file is used for local development.

Services started by Docker Compose:
- Frontend
- API Gateway
- Auth Service
- Product Service
- Inventory Service
- RabbitMQ
- PostgreSQL (auth_db)
- PostgreSQL (product_db)
- PostgreSQL (inventory_db)
- Prometheus + Grafana + cAdvisor

---

# RabbitMQ Scalability

The current architecture allows new consumers to be added without changing the Product Service.

```text
PRODUCT_CREATED
        |
        +--------------------+
        |                    |
        v                    v
Inventory Service    (Future: Analytics Service)
```

---

# Future Architecture

```text
                        +----------------+
                        | Angular Client |
                        +--------+-------+
                                 |
                                 v
                        +----------------+
                        |  API Gateway   |
                        +-------+--------+
                                |
            +-------------------+-------------------+
            |                   |                   |
            v                   v                   v
    +-------------+    +-------------+    +---------------+
    | Auth Service|    |  Product    |    |   Inventory   |
    | (RS256 JWT) |    |  Service    |    |   Service     |
    +------+------+    +------+------+    +-------+-------+
           |                   |                   |
           v                   v                   v
       RabbitMQ           RabbitMQ             RabbitMQ
```

Additional services will be introduced only after core concepts are mastered.

---

# Design Principles

- Start Simple
- Learn One Concept at a Time
- Infrastructure Only When Necessary
- Event-Driven Communication
- Loose Coupling
- Incremental Evolution
