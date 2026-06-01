# Task: Configure Inventory Service Database

Status: DONE

Dependencies: none

## Description

Create the Flyway migration for the inventory database and configure the Inventory Service for PostgreSQL access.

The inventory table must support the fields defined in specs/database/database-specs.md and specs/services/inventory-service.md.

## Acceptance Criteria

- [ ] V1__create_inventory_table.sql exists under `services/inventory-service/src/main/resources/db/migration/`
- [ ] Table `inventory` is created with columns: id (UUID PK), product_id (UUID, UNIQUE), product_name, quantity, reserved_quantity, version
- [ ] Unique index exists on product_id
- [ ] Index exists on updated_at
- [ ] The service starts without Flyway errors
- [ ] Hibernate validates the schema successfully
