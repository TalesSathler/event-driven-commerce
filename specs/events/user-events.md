# User Events

## Overview

User lifecycle events are published by the **Auth Service** when user accounts are created, updated, logged in, or deleted. Other services consume these events to synchronize user data or trigger workflows (e.g., audit logging, notification).

## Exchange

- **Name:** `auth.exchange`
- **Type:** `topic`
- **Durable:** yes

## Event Format

### Common Structure

All user events share a common `UserEvent` record:

| Field     | Type   | Description                                    |
|-----------|--------|------------------------------------------------|
| eventId   | UUID   | Unique event identifier                        |
| eventType | String | One of: USER_REGISTERED, USER_LOGIN, USER_UPDATED, USER_DELETED |
| userId    | UUID   | The affected user's ID                         |
| email     | String | The user's email address                       |
| role      | String | The user's role (USER, ADMIN)                  |
| timestamp | LocalDateTime | When the event occurred                  |

### JSON Example

```json
{
  "eventId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "eventType": "USER_REGISTERED",
  "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "email": "john@example.com",
  "role": "USER",
  "timestamp": "2025-01-01T00:00:00"
}
```

## Event Catalog

### USER_REGISTERED

- **Routing Key:** `user.registered`
- **Published when:** A new user completes registration
- **Payload:** Full user profile fields

### USER_LOGIN

- **Routing Key:** `user.login`
- **Published when:** A user successfully authenticates
- **Payload:** Full user profile fields

### USER_UPDATED

- **Routing Key:** `user.updated`
- **Published when:** A user's profile is modified
- **Payload:** Full user profile fields with updated values

### USER_DELETED

- **Routing Key:** `user.deleted`
- **Published when:** A user account is removed
- **Payload:** Minimal fields (userId, email, role) as the user record no longer exists

## Consumer Guidance

- Bind to `auth.exchange` with routing key `user.*` to receive all user events
- The `eventType` field can be used for dispatching without inspecting the routing key
- Events are not persisted — consume with idempotent handlers
