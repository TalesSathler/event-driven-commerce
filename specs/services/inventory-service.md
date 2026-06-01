# Inventory Service

## Responsibilities

- Consume product lifecycle events from RabbitMQ
- Maintain stock levels per product
- Expose inventory query and adjustment APIs
- Validate JWT tokens locally using RS256 public key
- Enforce role-based access control (admin, user)

## REST Endpoints

| Method | Path                          | Description                    | Auth Required | Roles     |
|--------|-------------------------------|--------------------------------|---------------|-----------|
| GET    | /api/inventory                | List all inventory items       | Yes           | user, admin |
| GET    | /api/inventory/{productId}    | Get inventory for a product    | Yes           | user, admin |
| PUT    | /api/inventory/{productId}    | Manually adjust stock quantity | Yes           | admin     |

## Request/Response Schemas

### GET /api/inventory

Response (200):

```json
[
  {
    "productId": "550e8400-e29b-41d4-a716-446655440000",
    "productName": "Wireless Mouse",
    "quantity": 100,
    "reservedQuantity": 0,
    "availableQuantity": 100,
    "version": 1
  }
]
```

### GET /api/inventory/{productId}

Response (200):

```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "productName": "Wireless Mouse",
  "quantity": 100,
  "reservedQuantity": 0,
  "availableQuantity": 100,
  "version": 1
}
```

### PUT /api/inventory/{productId}

```json
{
  "quantity": 50
}
```

Response (200):

```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "productName": "Wireless Mouse",
  "quantity": 50,
  "reservedQuantity": 0,
  "availableQuantity": 50,
  "version": 2
}
```

## Error Responses

| HTTP Status | Meaning                    |
|-------------|----------------------------|
| 400         | Validation failed          |
| 401         | Missing or invalid JWT     |
| 403         | Insufficient role          |
| 404         | Resource not found         |

```json
{
  "error": "Inventory not found"
}
```

## Consumed Events

| Event              | Routing Key        | Exchange          |
|--------------------|--------------------|-------------------|
| PRODUCT_CREATED    | product.created    | product.exchange  |
| PRODUCT_UPDATED    | product.updated    | product.exchange  |
| PRODUCT_DELETED    | product.deleted    | product.exchange  |

## Business Rules

- Inventory records are created when PRODUCT_CREATED is consumed
- `productName` is initially populated from the PRODUCT_CREATED event
- Inventory records are updated when PRODUCT_UPDATED is consumed (name changes)
- Quantity is NOT synced from events — inventory is managed independently via manual adjustments
- Inventory records are removed when PRODUCT_DELETED is consumed
- Manual adjustments via PUT override the current quantity
- Quantity must never go below zero
- Use optimistic locking (version field) to prevent concurrent overwrites
- JWT validation is performed locally using RS256 public key
- No authentication endpoints exist in this service
