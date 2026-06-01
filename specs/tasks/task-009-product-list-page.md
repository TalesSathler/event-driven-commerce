# Task: Implement Product List Page

Status: DONE

Dependencies: task-004-product-crud

## Description

Implement a product list page in the Angular frontend that displays all products in a grid/table format. The page must be behind authentication (auth guard).

Refer to specs/frontend/angular-frontend.md (Product List section and user flows).

## Acceptance Criteria

- [ ] Route `/products` exists, lazy-loaded
- [ ] Auth guard protects the route (redirects to /login if unauthenticated)
- [ ] Page fetches products from GET /api/products on load
- [ ] Products displayed in a responsive card grid or table
- [ ] Each product card shows: name, price, stock quantity
- [ ] "New Product" button navigates to /products/new
- [ ] Clicking a product navigates to /products/:id
- [ ] Loading state shown during fetch
- [ ] Empty state shown when no products exist
- [ ] Error state shown on fetch failure
