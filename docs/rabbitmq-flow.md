# RabbitMQ Event Flow

## Overview

RabbitMQ enables asynchronous communication between microservices. The Auth Service publishes user lifecycle events, the Product Service publishes product lifecycle events, and the Inventory Service (and future services) consume them.

## Exchanges

### Product Exchange

- **Name**: `product.exchange`
- **Type**: `topic`

### Auth Exchange

- **Name**: `auth.exchange`
- **Type**: `topic`

## Queues

### Inventory Queues (bound to product.exchange)

| Queue | Bound By | Routing Key Pattern |
|-------|----------|-------------------|
| `inventory.product.created` | Inventory Service | `product.created` |
| `inventory.product.updated` | Inventory Service | `product.updated` |
| `inventory.product.deleted` | Inventory Service | `product.deleted` |

## Event Contracts

### Product Events

```
Producer:   Product Service
Consumer:   Inventory Service
Routing:    product.created
Exchange:   product.exchange
Payload:
{
  "eventId": "uuid",
  "productId": "uuid",
  "name": "string",
  "price": 0.00,
  "timestamp": "2026-01-01T00:00:00Z"
}
```

### ProductUpdated

```
Producer:   Product Service
Consumer:   Inventory Service
Routing:    product.updated
Exchange:   product.exchange
Payload:
{
  "eventId": "uuid",
  "productId": "uuid",
  "name": "string",
  "price": 0.00,
  "timestamp": "2026-01-01T00:00:00Z"
}
```

### ProductDeleted

```
Producer:   Product Service
Consumer:   Inventory Service
Routing:    product.deleted
Exchange:   product.exchange
Payload:
{
  "eventId": "uuid",
  "productId": "uuid",
  "timestamp": "2026-01-01T00:00:00Z"
}
```

## User Events

### UserRegistered

```
Producer:   Auth Service
Routing:    user.registered
Exchange:   auth.exchange
Payload:
{
  "eventId": "uuid",
  "eventType": "USER_REGISTERED",
  "userId": "uuid",
  "email": "string",
  "role": "string",
  "timestamp": "2026-01-01T00:00:00"
}
```

### UserLogin

```
Producer:   Auth Service
Routing:    user.login
Exchange:   auth.exchange
Payload:
{
  "eventId": "uuid",
  "eventType": "USER_LOGIN",
  "userId": "uuid",
  "email": "string",
  "role": "string",
  "timestamp": "2026-01-01T00:00:00"
}
```

### UserUpdated

```
Producer:   Auth Service
Routing:    user.updated
Exchange:   auth.exchange
Payload:
{
  "eventId": "uuid",
  "eventType": "USER_UPDATED",
  "userId": "uuid",
  "email": "string",
  "role": "string",
  "timestamp": "2026-01-01T00:00:00"
}
```

### UserDeleted

```
Producer:   Auth Service
Routing:    user.deleted
Exchange:   auth.exchange
Payload:
{
  "eventId": "uuid",
  "eventType": "USER_DELETED",
  "userId": "uuid",
  "email": "string",
  "role": "string",
  "timestamp": "2026-01-01T00:00:00"
}
```

## Product Flow Diagram

```text
Product Service
      |
      | publish
      v
 product.exchange (topic)
      |
      | routing
      v
  +---+---+---+
  |       |       |
  v       v       v
created updated deleted
  |       |       |
  +---+---+---+
      |
      v
Inventory Service
```

## Auth Flow Diagram

```text
Auth Service
      |
      | publish
      v
 auth.exchange (topic)
      |
      | routing
      v
  +---+---+---+
  |       |       |
  v       v       v
registered login updated
  |       |       |
  +---+---+---+
      |
      v
Future Consumers
```

## Future

As new services are added, new queues bind to existing exchanges:

```text
 product.exchange          auth.exchange
      |                       |
      +-----------+           +-----------+
      |           |           |           |
      v           v           v           v
Inventory    Analytics     Audit      Notification
Service      Service       Service    Service
```
