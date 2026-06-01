# Technical Specifications

## API Gateway

### Package Structure

```
com.edc.gateway
└── GatewayServiceApplication.java
```

### Configuration

```yaml
server:
  port: 8080

spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: http://auth-service:8081
          predicates:
            - Path=/api/auth/**
        - id: product-service
          uri: http://product-service:8082
          predicates:
            - Path=/api/products/**
        - id: inventory-service
          uri: http://inventory-service:8083
          predicates:
            - Path=/api/inventory/**
```

---

## Auth Service

### Package Structure

```
com.edc.auth
├── config/
│   ├── SecurityConfig.java           — Spring Security filter chain
│   ├── CorsConfig.java               — CORS configuration
│   └── OpenAPIConfig.java            — Swagger/OpenAPI definition
├── auth/
│   ├── AuthController.java           — Register/login REST endpoints
│   ├── AuthService.java              — Auth business logic
│   ├── RegisterRequest.java          — Registration DTO
│   ├── LoginRequest.java             — Login DTO
│   ├── RefreshRequest.java           — Refresh token DTO
│   ├── AuthResponse.java             — Auth response DTO (includes tokens)
│   ├── TokenResponse.java            — Refresh response DTO
│   ├── EmailAlreadyExistsException.java
│   ├── InvalidCredentialsException.java
│   └── GlobalExceptionHandler.java   — Validation error handling
├── jwt/
│   ├── RsaKeyConfig.java             — RSA key pair generation/loading (2048-bit)
│   ├── JwtUtil.java                  — RS256 JWT generation and validation
│   └── PublicKeyController.java      — Expose public key + JWKS endpoint
├── token/
│   ├── RefreshToken.java             — Refresh token JPA entity
│   └── RefreshTokenRepository.java   — Refresh token data access
├── user/
│   ├── User.java                     — User JPA entity
│   └── UserRepository.java           — User data access
└── AuthServiceApplication.java
```

### Data Models

#### User Entity

| Field       | Type         | Constraints            |
|-------------|--------------|------------------------|
| id          | UUID (PK)    | Auto-generated         |
| name        | varchar(255) | NOT NULL               |
| email       | varchar(255) | NOT NULL, UNIQUE       |
| password    | varchar(255) | NOT NULL (BCrypt hash) |
| role        | varchar(20)  | NOT NULL, default 'USER' |
| createdAt   | timestamp    | NOT NULL, auto         |
| updatedAt   | timestamp    | NOT NULL, auto         |

#### Refresh Token Entity

| Field       | Type         | Constraints            |
|-------------|--------------|------------------------|
| id          | UUID (PK)    | Auto-generated         |
| userId      | UUID (FK)    | NOT NULL               |
| token       | varchar(512) | NOT NULL, UNIQUE       |
| expiresAt   | timestamp    | NOT NULL               |
| createdAt   | timestamp    | NOT NULL, auto         |

---

## Product Service

### Package Structure

```
com.edc.product
├── config/
│   ├── CorsConfig.java              — CORS configuration
│   ├── SecurityConfig.java          — Spring Security Resource Server config
│   ├── RabbitMQConfig.java          — Exchange declaration
│   └── OpenAPIConfig.java           — Swagger/OpenAPI definition
├── product/
│   ├── Product.java                 — Product JPA entity
│   ├── ProductRepository.java       — Product data access
│   ├── ProductController.java       — Product CRUD REST endpoints
│   ├── ProductService.java          — Product business logic
│   ├── ProductRequest.java          — Create/update product DTO
│   ├── ProductResponse.java         — Product response DTO
│   └── ProductNotFoundException.java
├── event/
│   ├── ProductEventPublisher.java   — RabbitMQ event publisher
│   └── ProductEvent.java            — Event DTO
└── ProductServiceApplication.java
```

### Data Models

#### Product Entity

| Field       | Type         | Constraints            |
|-------------|--------------|------------------------|
| id          | UUID (PK)    | Auto-generated         |
| name        | varchar(255) | NOT NULL               |
| description | text         | nullable               |
| price       | decimal(10,2)| NOT NULL, > 0          |
| quantity    | integer      | NOT NULL, >= 0         |
| createdAt   | timestamp    | NOT NULL, auto         |
| updatedAt   | timestamp    | NOT NULL, auto         |

---

## Inventory Service

### Package Structure

```
com.edc.inventory
├── config/
│   ├── SecurityConfig.java          — Spring Security Resource Server config
│   ├── RabbitMQConfig.java          — Queue and exchange declaration
│   └── OpenAPIConfig.java           — Swagger/OpenAPI definition
├── inventory/
│   ├── Inventory.java               — Inventory JPA entity
│   ├── InventoryRepository.java     — Inventory data access
│   ├── InventoryController.java     — Inventory REST endpoints
│   ├── InventoryService.java        — Inventory business logic
│   ├── InventoryAdjustRequest.java  — Stock adjustment DTO
│   ├── InventoryResponse.java       — Inventory response DTO
│   └── InventoryNotFoundException.java
├── event/
│   ├── ProductEventConsumer.java    — RabbitMQ consumer
│   └── ProductEvent.java            — Event DTO
└── InventoryServiceApplication.java
```

### Data Models

#### Inventory Entity

| Field             | Type         | Constraints                      |
|-------------------|--------------|----------------------------------|
| id                | UUID (PK)    | Auto-generated                   |
| productId         | UUID (unique)| NOT NULL (references product)    |
| productName       | varchar(255) | NOT NULL                         |
| quantity          | integer      | NOT NULL, >= 0                   |
| reservedQuantity  | integer      | NOT NULL, default 0              |
| version           | integer      | NOT NULL (optimistic locking)    |
| createdAt         | timestamp    | NOT NULL, auto                   |
| updatedAt         | timestamp    | NOT NULL, auto                   |

---

## JWT Configuration

| Property       | Value                       |
|----------------|-----------------------------|
| Algorithm      | RS256 (RSA Signature with SHA-256) |
| Key Size       | 2048 bits                   |
| Access Token   | 24 hours (86400000ms)       |
| Refresh Token  | 7 days (604800000ms)        |
| Claims         | sub (userId), email, roles, iat, exp, iss, aud |
| Header         | Authorization: Bearer <token> |
| Issuer         | auth-service                 |
| Audience       | product-service, inventory-service |

## Spring Security Resource Server Configuration

Services validate JWTs locally using an RS256 public key. The public key is fetched via JWKS URI from auth-service.

### Product Service

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: ${JWT_JWKS_URI:http://auth-service:8081/api/auth/jwks}
```

### Inventory Service

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: ${JWT_JWKS_URI:http://auth-service:8081/api/auth/jwks}
```

## Validation Rules

- `RegisterRequest.name`: @NotBlank
- `RegisterRequest.email`: @Email @NotBlank
- `RegisterRequest.password`: @NotBlank @Size(min = 6)
- `ProductRequest.name`: @NotBlank
- `ProductRequest.price`: @Positive @NotNull
- `ProductRequest.quantity`: @Min(0) @NotNull
- `InventoryAdjustRequest.quantity`: @Min(0) @NotNull

## Flyway Migrations

### Auth Service

| Migration                    | Content                          |
|------------------------------|----------------------------------|
| V1__create_users_table.sql   | CREATE TABLE users + index on email |
| V2__create_refresh_tokens_table.sql | CREATE TABLE refresh_tokens |

### Product Service

| Migration                    | Content                          |
|------------------------------|----------------------------------|
| V1__create_products_table.sql | CREATE TABLE products            |

### Inventory Service

| Migration                       | Content                           |
|---------------------------------|-----------------------------------|
| V1__create_inventory_table.sql   | CREATE TABLE inventory + unique index on product_id |
