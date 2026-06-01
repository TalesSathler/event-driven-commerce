# Task: Implement Inventory Entity and Repository

Status: DONE

Dependencies: task-001-inventory-database

## Description

Create the Inventory JPA entity and JPA repository in the Inventory Service. The entity must map to the `inventory` table with fields: id (UUID), productId (unique), productName, quantity, reservedQuantity, version, createdAt, updatedAt.

Use optimistic locking via `@Version` on the `version` field.

Refer to specs/tech-specs/tech-specs.md (Inventory Entity table) and specs/database/database-specs.md (inventory table).

## Acceptance Criteria

- [ ] `Inventory.java` entity exists in `com.edc.inventory.inventory` package
- [ ] Entity maps to `inventory` table
- [ ] `productId` is unique
- [ ] `@Version` annotation on `version` field for optimistic locking
- [ ] `@PrePersist` and `@PreUpdate` handle timestamps
- [ ] `InventoryRepository.java` extends JpaRepository
- [ ] Repository has `findByProductId(UUID)` method
- [ ] Hibernate schema validation passes on startup
