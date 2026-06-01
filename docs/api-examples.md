# API Examples

Example API requests and responses for the platform services.

All API requests go through the API Gateway at `http://localhost:8080`.

---

## Auth Service

Base URL: `http://localhost:8080/api/auth`

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

Response (201):

```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "dGhpcy1pcy1hLXJlZnJl...",
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "secret123"
}
```

Response (200):

```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9...",
  "refreshToken": "dGhpcy1pcy1hLXJlZnJl...",
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Refresh Token

```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "dGhpcy1pcy1hLXJlZnJl..."
}
```

Response (200):

```json
{
  "token": "eyJhbGciOiJSUzI1NiJ9..."
}
```

### Get Public Key

```http
GET /api/auth/public-key
```

Response (200): PEM-encoded public key.

### Get JWKS

```http
GET /api/auth/jwks
```

Response (200):
```json
{
  "keys": [
    {
      "kty": "RSA",
      "alg": "RS256",
      "use": "sig",
      "kid": "edc-rs256-key",
      "n": "...",
      "e": "AQAB"
    }
  ]
}
```

### Logout

```http
POST /api/auth/logout
Authorization: Bearer <token>
Content-Type: application/json

{
  "refreshToken": "dGhpcy1pcy1hLXJlZnJl..."
}
```

Response (204): No content.

### Get Current User

```http
GET /api/auth/me
Authorization: Bearer <token>
```

Response (200):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

---

## Product Service

Base URL: `http://localhost:8080/api/products`

All endpoints require `Authorization: Bearer <token>` header.

### List Products

```http
GET /api/products
Authorization: Bearer <token>
```

### Get Product by ID

```http
GET /api/products/{id}
Authorization: Bearer <token>
```

### Create Product (admin)

```http
POST /api/products
Authorization: Bearer <token>
Content-Type: application/json

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

### Update Product (admin)

```http
PUT /api/products/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Wireless Mouse Pro",
  "description": "Updated description",
  "price": 69.99,
  "quantity": 50
}
```

### Delete Product (admin)

```http
DELETE /api/products/{id}
Authorization: Bearer <token>
```

---

## Inventory Service

Base URL: `http://localhost:8080/api/inventory`

All endpoints require `Authorization: Bearer <token>` header.

### List Inventory

```http
GET /api/inventory
Authorization: Bearer <token>
```

### Get Inventory by Product ID

```http
GET /api/inventory/{productId}
Authorization: Bearer <token>
```

### Adjust Stock Quantity (admin)

```http
PUT /api/inventory/{productId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "quantity": 50
}
```
