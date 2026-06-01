# Product Service

## Responsibilities

- Manage product catalog (CRUD)
- Publish product lifecycle events to RabbitMQ
- Validate JWT tokens locally using RS256 public key
- Enforce role-based access control (admin, user)

## REST Endpoints

### Products (JWT-protected)

| Method | Path                | Description                      | Auth Required | Roles     |
|--------|---------------------|----------------------------------|---------------|-----------|
| GET    | /api/products       | List all products                | Yes           | user, admin |
| GET    | /api/products/{id}  | Get product by ID                | Yes           | user, admin |
| POST   | /api/products       | Create a new product             | Yes           | admin     |
| PUT    | /api/products/{id}  | Update an existing product       | Yes           | admin     |
| DELETE | /api/products/{id}  | Delete a product                 | Yes           | admin     |

## Request/Response Schemas

### GET /api/products

Response (200):

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 49.99,
    "quantity": 100,
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-01T00:00:00Z"
  }
]
```

### POST /api/products

```json
{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 49.99,
  "quantity": 100
}
```

Response (201):

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 49.99,
  "quantity": 100,
  "createdAt": "2026-01-01T00:00:00Z",
  "updatedAt": "2026-01-01T00:00:00Z"
}
```

### PUT /api/products/{id}

```json
{
  "name": "Wireless Mouse Pro",
  "description": "Updated description",
  "price": 69.99,
  "quantity": 50
}
```

Response (200): Full product object.

### DELETE /api/products/{id}

Response (204): No content.

## Error Responses

| HTTP Status | Meaning                    |
|-------------|----------------------------|
| 400         | Validation failed          |
| 401         | Missing or invalid JWT     |
| 403         | Insufficient role          |
| 404         | Resource not found         |
| 409         | Conflict (e.g. duplicate)  |

```json
{
  "error": "Product not found"
}
```

## Published Events

| Event              | Routing Key        | Exchange          |
|--------------------|--------------------|-------------------|
| PRODUCT_CREATED    | product.created    | product.exchange  |
| PRODUCT_UPDATED    | product.updated    | product.exchange  |
| PRODUCT_DELETED    | product.deleted    | product.exchange  |

## Business Rules

- Only authenticated users can view products (user role)
- Only admin users can create, update, or delete products
- Product name and price are required
- Price must be greater than zero
- Quantity must be non-negative
- Events are published after successful database write
- JWT validation is performed locally using RS256 public key
- No authentication endpoints exist in this service
