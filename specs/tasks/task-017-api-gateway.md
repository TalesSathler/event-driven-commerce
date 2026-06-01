# Task: API Gateway Service

Status: DONE

Dependencies: task-015-product-refactor-auth, task-016-inventory-add-security

## Description

Create an API Gateway service using Spring Cloud Gateway to centralize routing. The gateway forwards requests to auth-service, product-service, and inventory-service based on path.

## Acceptance Criteria

- [ ] `services/gateway/pom.xml` with Spring Cloud Gateway dependency
- [ ] Route: `/api/auth/**` → auth-service:8081
- [ ] Route: `/api/products/**` → product-service:8082
- [ ] Route: `/api/inventory/**` → inventory-service:8083
- [ ] Route: `/swagger-ui/**` → individual service (or aggregate)
- [ ] Route: `/v3/api-docs/**` → individual service (or aggregate)
- [ ] CORS configured for frontend origin
- [ ] `Dockerfile` with Maven build
- [ ] `application.yml` with route definitions
- [ ] Gateway port: 8080
