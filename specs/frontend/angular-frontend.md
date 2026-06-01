# Frontend Specifications

## Technology Stack

- Angular (standalone components, no NgModules)
- Nx workspace with @angular/build:dev-server (Vite)
- Tailwind CSS 4
- Zard UI component library
- Angular Router (lazy-loaded pages)
- Angular HttpClient with functional interceptors

## Pages

### Home (`/`)

The landing page for the platform. Shows:
- Hero section with project name and tagline
- Tech stack overview
- Architecture diagram (API Gateway + Auth/Product/Inventory services)
- Roadmap
- Getting started guide
- Navigation to Login/Register (when unauthenticated)

### Login (`/login`)

A form with email and password fields. On success, stores the JWT token and redirects to home. If the user is already authenticated, redirects to home (guestGuard).

### Register (`/register`)

A form with name, email, and password fields. On success, stores the JWT token and redirects to home. If the user is already authenticated, redirects to home (guestGuard).

### Product List (`/products`) — DONE

Displays all products in a grid or table. Each product card shows name, price, and stock quantity. Provides a button to create a new product and links to edit/delete each product.

### Product Detail (`/products/:id`) — DONE

Shows full product information. Provides buttons to edit or delete.

### Product Create/Edit (`/products/new`, `/products/:id/edit`) — DONE

A form with fields for name, description, price, and quantity. Uses the same form component for both create and edit modes.

## User Flows

### Registration Flow

1. User navigates to `/register`
2. If already authenticated → redirected to `/`
3. User fills in name, email, password
4. On submit → POST `/api/auth/register` (via API Gateway)
5. On success → JWT stored in localStorage → redirect to `/`
6. On error → display error message

### Login Flow

1. User navigates to `/login`
2. If already authenticated → redirected to `/`
3. User fills in email, password
4. On submit → POST `/api/auth/login` (via API Gateway)
5. On success → JWT stored in localStorage → redirect to `/`
6. On error → display error message

### Product Management Flow

1. User is authenticated (otherwise redirected to `/login`)
2. User navigates to `/products` to see all products
3. User clicks "New Product" → navigates to `/products/new`
4. User fills in product details → submits
5. On success → redirected to product list with new product visible
6. User clicks a product → navigates to `/products/:id`
7. User clicks "Edit" → navigates to `/products/:id/edit`
8. User clicks "Delete" → confirmation dialog → product removed

## State Management

- JWT token stored in localStorage under `edc_token`
- User name stored in localStorage under `edc_name`
- User email stored in localStorage under `edc_email`
- Auth guard checks token existence for protected routes
- Guest guard checks token and redirects authenticated users away from login/register
- HTTP interceptor attaches `Authorization: Bearer <token>` header to all requests

## Services

| Service            | Endpoint                         | Methods           |
|--------------------|----------------------------------|-------------------|
| AuthService        | /api/auth                        | register, login, logout |
| ProductService     | /api/products                    | getAll, getById, create, update, delete |
| InventoryService   | /api/inventory                   | getByProductId, adjustQuantity |

## API Integration

All API requests go through the API Gateway at `http://localhost:8080`.

Base URLs (from environment files):

- Development: all services point to `http://localhost:8080/api` (single gateway URL)
- Auth, product, and inventory paths are routed by the gateway based on path prefix
- Swagger UIs point directly to individual service ports for separate OpenAPI docs
