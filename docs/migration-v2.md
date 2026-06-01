# Migration Guide: v2 Architecture

## Overview

Version 2 introduces a dedicated Auth Service, RS256 JWT signing, and an API Gateway. Authentication is extracted from product-service into its own microservice.

## Breaking Changes

### 1. Port Changes

| Service            | Old Port | New Port |
|--------------------|----------|----------|
| API Gateway        | —        | 8080     |
| Auth Service       | —        | 8081     |
| Product Service    | 8080     | 8082     |
| Inventory Service  | 8081     | 8083     |
| PostgreSQL Product | 5432     | 5433     |
| PostgreSQL Inv.    | 5433     | 5434     |
| PostgreSQL Auth    | —        | 5432     |

### 2. Authentication Removed from Product Service

- `POST /api/auth/register` — moved to auth-service
- `POST /api/auth/login` — moved to auth-service
- User entity and repository — moved to auth-service
- JWT now uses RS256 instead of HMAC-SHA512
- Existing JWTs issued by product-service are invalid after migration

### 3. New Auth Service

- Endpoint: `POST /api/auth/register`
- Endpoint: `POST /api/auth/login`
- Endpoint: `POST /api/auth/refresh`
- Endpoint: `GET /api/auth/public-key`
- Owns the `users` table (moved from product_db to auth_db)
- Uses RS256 (RSA) instead of HMAC-SHA512

### 4. API Gateway

- All traffic now routes through the gateway at port 8080
- Frontend must update API base URL to point to the gateway
- Path-based routing: `/api/auth/**` → auth-service, `/api/products/**` → product-service, etc.

## Migration Steps

### Database

1. Export users from product_db before migrating
2. Run auth-service Flyway migrations to create auth_db schema
3. Import users into auth_db
4. Drop `V2__create_users_table.sql` from product-service migrations
5. Remove users table from product_db

### Services

1. Deploy auth-service first
2. Rebuild product-service with auth code removed
3. Rebuild inventory-service with Resource Server config
4. Deploy API Gateway
5. Update frontend environment to point to gateway

## Rollback

Stop all new services, restore old docker-compose.yml, restore product-service with auth code, restore old database schemas.
