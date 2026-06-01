# Database Specifications

## Ownership

| Database       | Owner Service     | Host (Docker)          | Port  |
|----------------|-------------------|------------------------|-------|
| auth_db        | Auth Service      | edc-postgres-auth      | 5432  |
| product_db     | Product Service   | edc-postgres-product   | 5433  |
| inventory_db   | Inventory Service | edc-postgres-inventory | 5434  |

No direct cross-service database access. All inter-service communication is through REST APIs or RabbitMQ events.

---

## Auth Database (auth_db)

### Entity: users

| Column       | Type            | Constraints                      |
|--------------|-----------------|----------------------------------|
| id           | UUID            | PRIMARY KEY                      |
| name         | VARCHAR(255)    | NOT NULL                         |
| email        | VARCHAR(255)    | NOT NULL, UNIQUE                 |
| password     | VARCHAR(255)    | NOT NULL                         |
| role         | VARCHAR(20)     | NOT NULL, DEFAULT 'USER'         |
| created_at   | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| updated_at   | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP |

### Entity: refresh_tokens

| Column       | Type            | Constraints                              |
|--------------|-----------------|------------------------------------------|
| id           | UUID            | PRIMARY KEY                              |
| user_id      | UUID            | NOT NULL, FOREIGN KEY (users.id)         |
| token        | VARCHAR(512)    | NOT NULL, UNIQUE                         |
| expires_at   | TIMESTAMP       | NOT NULL                                 |
| created_at   | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP      |

### Indexes

```sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
```

---

## Product Database (product_db)

### Entity: products

| Column       | Type            | Constraints                      |
|--------------|-----------------|----------------------------------|
| id           | UUID            | PRIMARY KEY                      |
| name         | VARCHAR(255)    | NOT NULL                         |
| description  | TEXT            | NULLABLE                         |
| price        | DECIMAL(10, 2)  | NOT NULL                         |
| stock_quantity| INTEGER        | NOT NULL, DEFAULT 0              |
| created_at   | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP |
| updated_at   | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP |

---

## Inventory Database (inventory_db)

### Entity: inventory

| Column            | Type            | Constraints                              |
|-------------------|-----------------|------------------------------------------|
| id                | UUID            | PRIMARY KEY                              |
| product_id        | UUID            | NOT NULL, UNIQUE                         |
| product_name      | VARCHAR(255)    | NOT NULL                                 |
| quantity          | INTEGER         | NOT NULL, DEFAULT 0, CHECK >= 0          |
| reserved_quantity | INTEGER         | NOT NULL, DEFAULT 0                      |
| version           | INTEGER         | NOT NULL, DEFAULT 0                      |
| created_at        | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP      |
| updated_at        | TIMESTAMP       | NOT NULL, DEFAULT CURRENT_TIMESTAMP      |

### Indexes

```sql
CREATE UNIQUE INDEX idx_inventory_product_id ON inventory(product_id);
CREATE INDEX idx_inventory_updated_at ON inventory(updated_at);
```

---

## Migration Strategy

All services use Flyway for schema management with `ddl-auto: validate` in Hibernate.

Migrations are versioned SQL files under `src/main/resources/db/migration/`:

```
Auth Service:
V1__create_users_table.sql
V2__create_refresh_tokens_table.sql

Product Service:
V1__create_products_table.sql

Inventory Service:
V1__create_inventory_table.sql
```
