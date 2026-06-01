# Auth Service

## Responsibilities

- Manage user registration and authentication
- Issue JWT tokens (RS256-signed)
- Manage refresh tokens
- Never expose private keys to other services
- Publish user lifecycle events to RabbitMQ

## REST Endpoints

### Authentication (public)

| Method | Path               | Description                | Auth Required |
|--------|--------------------|----------------------------|---------------|
| POST   | /api/auth/register | Register a new user        | No            |
| POST   | /api/auth/login    | Authenticate and get JWT   | No            |
| POST   | /api/auth/refresh  | Refresh an expired JWT     | No            |

### Authentication (authenticated)

| Method | Path             | Description                | Auth Required |
|--------|------------------|----------------------------|---------------|
| POST   | /api/auth/logout | Invalidate a refresh token | Yes           |
| GET    | /api/auth/me     | Get current user profile   | Yes           |

### Public Key (public)

| Method | Path                  | Description                            |
|--------|-----------------------|----------------------------------------|
| GET    | /api/auth/public-key  | RSA public key in PEM format           |
| GET    | /api/auth/jwks        | JSON Web Key Set for JWT verification  |

## Request/Response Schemas

### POST /api/auth/register

```json
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

### POST /api/auth/login

```json
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

### POST /api/auth/refresh

```json
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

### POST /api/auth/logout

```json
{
  "refreshToken": "dGhpcy1pcy1hLXJlZnJl..."
}
```

Response (204): No content.

### GET /api/auth/me

Response (200):

```json
{
  "id": "uuid-string",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

### GET /api/auth/public-key

Response (200, text/plain): PEM-encoded RSA public key.

### GET /api/auth/jwks

Response (200, application/json): JWKS with a single key entry.

## Error Responses

| HTTP Status | Meaning                    |
|-------------|----------------------------|
| 400         | Validation failed          |
| 401         | Invalid credentials / missing JWT |
| 409         | Conflict (email exists)    |

```json
{
  "error": "Email already registered"
}
```

## Published Events

| Event              | Routing Key        | Exchange          |
|--------------------|--------------------|-------------------|
| USER_REGISTERED    | user.registered    | auth.exchange     |
| USER_LOGIN         | user.login         | auth.exchange     |
| USER_UPDATED       | user.updated       | auth.exchange     |
| USER_DELETED       | user.deleted       | auth.exchange     |

### UserEvent Schema

```json
{
  "eventId": "uuid",
  "eventType": "USER_REGISTERED",
  "userId": "uuid",
  "email": "john@example.com",
  "role": "USER",
  "timestamp": "2025-01-01T00:00:00"
}
```

## Business Rules

- Email must be unique per user
- Passwords must be BCrypt-encoded before storage
- Password column is named `password_hash` in the database
- JWT tokens use RS256 (RSA Signature with SHA-256)
- Token expiration: 24 hours for access tokens
- Refresh token expiration: 7 days
- Private key is never exposed to other services
- Public key is exposed via a dedicated endpoint: `GET /api/auth/public-key`
- Logout invalidates (deletes) the provided refresh token
- Me endpoint returns user profile from the JWT's `sub` claim (user ID)
