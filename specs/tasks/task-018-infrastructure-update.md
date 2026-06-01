# Task: Update Docker Compose and Configuration

Status: DONE

Dependencies: task-017-api-gateway

## Description

Update docker-compose.yml, .env.example, and all configuration files to reflect the new architecture: auth-service, API Gateway, updated ports, and new PostgreSQL instance.

## Acceptance Criteria

- [ ] `docker-compose.yml` updated with auth-service service
- [ ] `docker-compose.yml` updated with API Gateway service
- [ ] `docker-compose.yml` updated with edc-postgres-auth database
- [ ] Service ports updated: gateway 8080, auth 8081, product 8082, inventory 8083
- [ ] `depends_on` and network config updated
- [ ] `.env.example` updated with new service credentials
- [ ] Environment variables in docker-compose.yml updated for new service names and ports
- [ ] Frontend environment files updated with new API gateway URL
- [ ] `services/product-service/src/main/resources/application.yml` updated with new DB host/port
- [ ] `services/inventory-service/src/main/resources/application.yml` updated with new DB host/port
