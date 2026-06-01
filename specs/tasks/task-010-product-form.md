# Task: Implement Product Create/Edit Form

Status: DONE

Dependencies: task-004-product-crud

## Description

Implement a shared product form component that supports both creating and editing products. The form must be behind authentication.

Refer to specs/frontend/angular-frontend.md (Product Create/Edit section) and specs/services/product-service.md for field definitions.

## Acceptance Criteria

- [ ] Route `/products/new` exists for creating
- [ ] Route `/products/:id/edit` exists for editing
- [ ] Form fields: name, description (textarea), price, quantity
- [ ] Name and price are required, validated on submit
- [ ] Price must be positive, quantity non-negative
- [ ] Create mode: POST /api/products, redirect to product list on success
- [ ] Edit mode: pre-fills form from GET /api/products/:id, PUT /api/products/:id on submit
- [ ] Loading state during submit
- [ ] Validation errors displayed inline
- [ ] Cancel button navigates back to product list
