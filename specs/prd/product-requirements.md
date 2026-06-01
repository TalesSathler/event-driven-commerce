# Product Requirements Document — Event-Driven Commerce

## Overview

Event-Driven Commerce is a microservices-based e-commerce platform built to learn and demonstrate Spring Boot microservices, RabbitMQ event-driven architecture, JWT authentication, and Angular frontend integration.

## Goals

- Learn Spring Boot microservices with real inter-service communication
- Implement event-driven patterns with RabbitMQ (topic exchanges, routing keys, queues)
- Build a working Angular SPA that consumes microservice APIs
- Practice Database per Service pattern with PostgreSQL
- Implement JWT-based authentication with BCrypt

## Functional Requirements

### FR1 - User Registration
Users must register with name, email, and password. Passwords are BCrypt-encoded. Duplicate emails are rejected.

### FR2 - User Login
Registered users must authenticate with email and password. A JWT token is returned upon successful login.

### FR3 - Product CRUD (JWT-protected)
Authenticated users must be able to create, read, update, and delete products. Each product has a name, description, price, and stock quantity.

### FR4 - Product Event Publishing
Product creation, update, and deletion must publish corresponding events to RabbitMQ (PRODUCT_CREATED, PRODUCT_UPDATED, PRODUCT_DELETED).

### FR5 - Inventory Tracking
The Inventory Service must consume product events and maintain stock levels per product. It must expose an API to query and manually adjust inventory.

### FR6 - Frontend Product Management
The Angular frontend must provide a product list view, product detail view, product creation form, and product edit form, all behind authentication.

## Non-Functional Requirements

### NFR1 - Service Isolation
Each service owns its database. No direct database sharing. Communication happens through REST APIs and RabbitMQ events.

### NFR2 - Async Communication
Product events must be published asynchronously through RabbitMQ to ensure loose coupling between Product Service and Inventory Service.

### NFR3 - Security
All product endpoints require JWT authentication. Passwords are hashed with BCrypt. JWT tokens expire after 24 hours.

### NFR4 - Idempotency
The Inventory Service must handle duplicate events gracefully (upsert semantics for stock records).

### NFR5 - Local Development
The entire platform runs locally via Docker Compose with hot-reload for the Angular frontend.

## Sprint Roadmap

### Sprint 1 (Current)
- User registration and login
- Product CRUD API
- Product management UI (list, create, edit)
- JWT authentication on all protected endpoints

### Sprint 2
- RabbitMQ infrastructure (exchange, queues)
- PRODUCT_CREATED event producer and consumer
- Inventory Service stock tracking

### Sprint 3
- PRODUCT_UPDATED and PRODUCT_DELETED events
- Inventory sync for all product lifecycle events

### Sprint 4
- Retry and Dead Letter Queue patterns
- Error handling and observability

### Sprint 5
- Order Service
- ORDER_CREATED event
- Inventory reservation
