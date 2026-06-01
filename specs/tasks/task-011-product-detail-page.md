# Task: Implement Product Detail Page

Status: DONE

Dependencies: task-004-product-crud

## Description

Implement a product detail page that displays full product information with edit and delete actions. The page must be behind authentication.

Refer to specs/frontend/angular-frontend.md (Product Detail section) and specs/services/product-service.md for response schema.

## Acceptance Criteria

- [ ] Route `/products/:id` exists, lazy-loaded
- [ ] Auth guard protects the route
- [ ] Page fetches product from GET /api/products/:id on load
- [ ] Displays: name, description, price, quantity, created date
- [ ] "Edit" button navigates to /products/:id/edit
- [ ] "Delete" button shows confirmation dialog
- [ ] On delete confirmation: DELETE /api/products/:id, redirect to product list
- [ ] 404 state shown when product not found
- [ ] Loading state during fetch
- [ ] Error state shown on fetch failure
