# Task: Inventory Service — Add JWT Validation

Status: DONE

Dependencies: task-014-rsa-key-management

## Description

Add JWT validation to inventory-service using Spring Security Resource Server. All inventory endpoints become JWT-protected with role-based access.

## Acceptance Criteria

- [ ] `spring-security-oauth2-resource-server` dependency added to `pom.xml`
- [ ] `SecurityConfig.java` created with Resource Server config
- [ ] All inventory endpoints require valid JWT
- [ ] `GET /api/inventory/**` accessible to roles: user, admin
- [ ] `PUT /api/inventory/{productId}` requires admin role
- [ ] JWT is validated locally with RS256 public key (no runtime calls to auth-service)
- [ ] 401 returned for missing/invalid JWTs
- [ ] 403 returned for insufficient roles
- [ ] `application.yml` configured with RS256 public key location
- [ ] Tests: `InventoryControllerTest` updated with mock JWT
- [ ] Swagger/OpenAPI updated: JWT Bearer auth
