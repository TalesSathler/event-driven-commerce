# RabbitMQ

RabbitMQ 3 with Management Plugin for asynchronous event-driven communication between services.

## Access

- **AMQP**: `localhost:5672`
- **Management UI**: `http://localhost:15672` (login: admin/admin)

## Configuration

- `rabbitmq.conf` — server config (loopback_users, listeners, management port)
- Exchanges and queues are declared programmatically by each service on startup

## Event Flow

```
Product Service ──publish──> RabbitMQ ──consume──> Inventory Service
```

Planned events: `ProductCreated`, `ProductUpdated`, `ProductDeleted`.
