# Task: Implement Inventory REST Controller

Status: DONE

Dependencies: task-006-inventory-entity

## Description

Implement the Inventory REST controller for querying and manually adjusting stock levels.

Refer to specs/services/inventory-service.md for endpoint definitions, request/response schemas, and business rules.

## Acceptance Criteria

- [ ] `InventoryController.java` exists in `com.edc.inventory.inventory` package
- [ ] `InventoryService.java` exists with business logic
- [ ] `InventoryNotFoundException.java` custom exception
- [ ] GET /api/inventory returns all inventory items (200)
- [ ] GET /api/inventory/{productId} returns inventory for a product (200) or 404
- [ ] PUT /api/inventory/{productId} adjusts stock quantity (200) or 404
- [ ] Quantity never goes below zero (400 if adjustment would cause negative)
- [ ] Version field is checked for optimistic locking
- [ ] Response includes `availableQuantity` computed as `quantity - reservedQuantity`
