# Event-Driven Commerce

A learning-focused Event-Driven E-Commerce Platform built with modern backend and frontend technologies.

The primary goal of this project is to gain hands-on experience with:

* Event-Driven Architecture (EDA)
* RabbitMQ
* Microservices
* Spring Boot 3
* Angular
* PostgreSQL
* Docker
* Spec-Driven Development (SDD)

---

# Objectives

This project is designed as a portfolio project and learning platform.

Key goals:

* Build a scalable microservices architecture
* Learn asynchronous communication using RabbitMQ
* Implement JWT authentication and authorization (RS256)
* Practice domain-driven service boundaries
* Apply Spec-Driven Development with AI-assisted workflows
* Containerize the entire platform using Docker

---

# Technology Stack

## Backend

* Java 21
* Spring Boot 3
* Spring Security + OAuth2 Resource Server
* Spring Data JPA
* Spring AMQP
* JWT Authentication (RS256)
* Spring Cloud Gateway
* Maven

## Databases

* PostgreSQL (database per service)

## Messaging

* RabbitMQ

## Frontend

* Angular

## Infrastructure

* Docker
* Docker Compose

## Monitoring & Observability

* Prometheus (metrics aggregation)
* Grafana (dashboards & visualization)
* cAdvisor (container metrics)
* PostgreSQL Exporter (database metrics)
* RabbitMQ Prometheus Plugin (broker metrics)
* Spring Boot Actuator + Micrometer (JVM & HikariCP metrics)

## Development Process

* Spec-Driven Development (SDD)
* OpenCode CLI
* AI-Assisted Development

---

# Architecture

All traffic flows through the API Gateway, which routes to downstream services.

```text
                       +-------------------+
                       |   Angular SPA     |
                       |   localhost:4200   |
                       +---------+---------+
                                 |
                           HTTP (REST)
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
       |   Auth Service  |  |  Product  |  |   Inventory     |
       |   :8081         |  |  Service  |  |   Service       |
       |                 |  |  :8082    |  |   :8083         |
       +--------+--------+  +-----+-----+  +--------+--------+
                |                  |                 |
           RabbitMQ          RabbitMQ           RabbitMQ
                |                  |                 |
                v                  v                 v
       +----------------+  +--------------+  +------------------+
       |  PostgreSQL    |  |  PostgreSQL  |  |   PostgreSQL     |
       |  auth_db       |  |  product_db  |  |   inventory_db   |
       |  :5432         |  |  :5433       |  |   :5434          |
       +----------------+  +--------------+  +------------------+

                 +--------+--------+
                 |                  |
                 v                  v
          +------------+    +------------+
          | Prometheus |    |  Grafana   |
          | :9090      |    |  :3000     |
          +------------+    +------------+
```

## Services

### Auth Service

Responsibilities:
- User registration, login, and logout
- JWT issuance (RS256-signed) and refresh
- Current user profile (`GET /auth/me`)
- Private key management (never exposed)
- Public key at `GET /api/auth/public-key`, JWKS at `GET /api/auth/jwks`
- Publish user lifecycle events (`USER_REGISTERED`, `USER_LOGIN`) to RabbitMQ

### Product Service

Responsibilities:
- Product catalog management (CRUD)
- Publish product lifecycle events to RabbitMQ
- Validate JWTs locally via RS256 Resource Server (no runtime calls to auth-service)
- Role-based access: GET requires user/admin, write requires admin

### Inventory Service

Responsibilities:
- Inventory/stock management
- Consume product events from RabbitMQ
- Validate JWTs locally via RS256 Resource Server
- Role-based access: GET requires user/admin, adjust requires admin

---

# Event-Driven Communication

RabbitMQ is used for asynchronous communication between services.

## Product Events

```text
Create Product
      |
      v
Product Service
      |
      v
Publish ProductCreated Event
      |
      v
RabbitMQ (topic: product.exchange)
      |
      v
Inventory Service (creates inventory record)
```

Events:
- PRODUCT_CREATED (routing key: product.created)
- PRODUCT_UPDATED (routing key: product.updated)
- PRODUCT_DELETED (routing key: product.deleted)

Exchange: `product.exchange` (topic)

## User Events

```text
User Registration / Login
      |
      v
Auth Service
      |
      v
Publish User Event
      |
      v
RabbitMQ (topic: auth.exchange)
```

Events:
- USER_REGISTERED (routing key: user.registered)
- USER_LOGIN (routing key: user.login)
- USER_UPDATED (routing key: user.updated)
- USER_DELETED (routing key: user.deleted)

Exchange: `auth.exchange` (topic)

---

# Project Structure

```text
.
├── AGENTS.md
├── README.md
├── docker-compose.yml
├── .env.example
│
├── docs/
│
├── workflows/
│
├── specs/
│
├── frontend/
│   └── Dockerfile
│
├── services/
│   ├── gateway/
│   │   └── Dockerfile
│   ├── auth-service/
│   │   └── Dockerfile
│   ├── product-service/
│   │   └── Dockerfile
│   └── inventory-service/
│       └── Dockerfile
│
└── docker/
    ├── rabbitmq/
    ├── prometheus/
    │   └── prometheus.yml
    └── grafana/
        ├── datasource.yml
        └── dashboards/
```

---

# Development Methodology

This project follows Spec-Driven Development (SDD).

Workflow:

```text
Idea
 ↓
Planning
 ↓
Refinement
 ↓
Task Generation
 ↓
Implementation
 ↓
Review
```

All implementation work must originate from approved specifications.

---

# Documentation

Project documentation is organized as follows:

```text
docs/
├── ai-workflow.md
├── api-examples.md
├── architecture.md
├── rabbitmq-flow.md
├── spec-driven-development.md
└── migration-v2.md
```

Specifications are stored in:

```text
specs/
├── prd/
├── architecture/
├── services/
├── tech-specs/
├── events/
├── database/
├── frontend/
├── decisions/
├── tasks/
└── reviews/
```

---

# Getting Started

## Prerequisites

- Docker & Docker Compose
- Java 21+ (for local development)
- Maven 3.9+ (for local development)

## Clone Repository

```bash
git clone <repository-url>
cd event-driven-commerce
```

## Start All Services

```bash
docker compose up -d
```

Wait for all services to initialize (especially databases), then verify:

```bash
docker compose ps
```

## Access Points

| Service | URL | Credentials |
|---|---|---|
| Frontend (Angular) | `http://localhost:4200` | — |
| API Gateway | `http://localhost:8080` | — |
| Auth Service | `http://localhost:8081` | — |
| Product API | `http://localhost:8082` | JWT (Bearer) |
| Inventory API | `http://localhost:8083` | JWT (Bearer) |
| RabbitMQ Management | `http://localhost:15672` | admin / admin |
| Prometheus | `http://localhost:9090` | — |
| Grafana | `http://localhost:3000` | admin / admin |
| cAdvisor | `http://localhost:8084` | — |

---

# API Usage

All API requests (except auth endpoints) require a JWT Bearer token.

## Auth

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@example.com","password":"secret123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"secret123"}'

# Refresh token
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<refresh-token>"}'

# Logout (requires JWT)
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"refreshToken":"<refresh-token>"}'

# Get current user profile (requires JWT)
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <token>"
```

## Products

```bash
# List products (requires JWT)
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>"
```

## Inventory

```bash
# Get inventory (requires JWT)
curl http://localhost:8080/api/inventory \
  -H "Authorization: Bearer <token>"
```

---

# License

This project is intended for educational and portfolio purposes.
