# Event Contracts

## Product Events

Published by Product Service to `product.exchange` (topic).

### ProductCreated

| Field     | Type     | Description                    |
|-----------|----------|--------------------------------|
| eventId   | UUID     | Unique event identifier        |
| eventType | string   | `PRODUCT_CREATED`              |
| productId | UUID     | Created product ID             |
| name      | string   | Product name                   |
| price     | decimal  | Product price                  |
| timestamp | instant  | Event timestamp                |

### ProductUpdated

| Field     | Type     | Description                    |
|-----------|----------|--------------------------------|
| eventId   | UUID     | Unique event identifier        |
| eventType | string   | `PRODUCT_UPDATED`              |
| productId | UUID     | Updated product ID             |
| name      | string   | New product name               |
| price     | decimal  | New product price              |
| timestamp | instant  | Event timestamp                |

### ProductDeleted

| Field     | Type     | Description                    |
|-----------|----------|--------------------------------|
| eventId   | UUID     | Unique event identifier        |
| eventType | string   | `PRODUCT_DELETED`              |
| productId | UUID     | Deleted product ID             |
| timestamp | instant  | Event timestamp                |

## Auth Events (Future)

Published by Auth Service to `auth.exchange` (topic).

### UserRegistered

| Field     | Type     | Description                    |
|-----------|----------|--------------------------------|
| eventId   | UUID     | Unique event identifier        |
| eventType | string   | `USER_REGISTERED`              |
| userId    | UUID     | Registered user ID             |
| email     | string   | User email                     |
| role      | string   | User role                      |
| timestamp | instant  | Event timestamp                |
