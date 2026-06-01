# Task: Update Documentation

Status: DONE

Dependencies: task-018-infrastructure-update

## Description

Update all documentation to reflect the new architecture: README, specs, architecture diagrams, Swagger/OpenAPI, and migration notes.

## Acceptance Criteria

- [ ] `README.md` updated with auth-service, API Gateway, new port mapping
- [ ] Architecture diagram in `README.md` shows Gateway → Services flow
- [ ] Access points table updated with all service URLs and ports
- [ ] Getting Started instructions verified with new docker-compose
- [ ] `docs/architecture.md` updated with new trust boundaries and auth flow
- [ ] `docs/api-examples.md` updated (remove auth from product, add auth-service)
- [ ] Swagger/OpenAPI for auth-service documents register, login, refresh
- [ ] Swagger/OpenAPI for product-service has JWT Bearer auth, no auth endpoints
- [ ] Swagger/OpenAPI for inventory-service has JWT Bearer auth
- [ ] Migration notes created at `docs/migration-v2.md` documenting breaking changes
