# Task: Implement Product CRUD Service and Controller

Status: DONE

Dependencies: task-003-product-entity

## Description

Implement the Product service and REST controller with full CRUD operations. All endpoints must be JWT-protected except for authentication routes.

Refer to specs/services/product-service.md for endpoint definitions and request/response schemas. Refer to specs/tech-specs/tech-specs.md for package structure and validation rules.

## Acceptance Criteria

- [ ] `ProductController.java` exists in `com.edc.product.product` package
- [ ] `ProductService.java` exists with business logic
- [ ] `ProductRequest.java` DTO with @NotBlank name, @Positive price, @Min(0) quantity
- [ ] `ProductResponse.java` DTO matching the spec response schema
- [ ] `ProductNotFoundException.java` custom exception
- [ ] GET /api/products returns all products (200)
- [ ] GET /api/products/{id} returns a single product (200) or 404
- [ ] POST /api/products creates a product (201)
- [ ] PUT /api/products/{id} updates a product (200) or 404
- [ ] DELETE /api/products/{id} deletes a product (204) or 404
- [ ] All endpoints return 401 without valid JWT
- [ ] Validation returns 400 for invalid input
