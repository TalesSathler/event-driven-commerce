# Task: Implement Product Entity and Repository

Status: DONE

Dependencies: none

## Description

Create the Product JPA entity and JPA repository in the Product Service. The product entity must map to the `products` table with fields: id (UUID), name, description, price, quantity (maps to `stock_quantity`), createdAt, updatedAt.

Refer to specs/tech-specs/tech-specs.md (Product Entity table) and specs/database/database-specs.md (products table).

## Acceptance Criteria

- [ ] `Product.java` entity exists in `com.edc.product.product` package
- [ ] Entity maps to `products` table
- [ ] All columns defined with correct types and constraints
- [ ] `@PrePersist` and `@PreUpdate` handle timestamps
- [ ] `ProductRepository.java` extends JpaRepository
- [ ] Repository supports findById, findAll, save, delete
- [ ] Hibernate schema validation passes on startup
