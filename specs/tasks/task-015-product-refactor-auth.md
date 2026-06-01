# Task: Product Service — Remove Authentication, Add Resource Server

Status: DONE

Dependencies: task-014-rsa-key-management

## Description

Refactor product-service to remove all authentication concerns. Delete user entity, auth controller, auth service, user-related DTOs, and JwtUtil. Replace with Spring Security Resource Server configured for RS256 JWT validation.

## Acceptance Criteria

- [ ] `User.java` entity deleted
- [ ] `UserRepository.java` deleted
- [ ] `AuthController.java` deleted
- [ ] `AuthService.java` deleted (or moved to auth-service)
- [ ] `JwtUtil.java` deleted
- [ ] `JwtAuthFilter.java` deleted
- [ ] `V2__create_users_table.sql` migration is removed (auth service owns users)
- [ ] `SecurityConfig.java` updated to use `spring-security-oauth2-resource-server`
- [ ] `application.yml` configured with RS256 public key location
- [ ] `spring-security-oauth2-resource-server` dependency added to `pom.xml`
- [ ] `jjwt` dependency removed from `pom.xml`
- [ ] All product endpoints require valid JWT
- [ ] JWT is validated locally with RS256 public key (no runtime calls to auth-service)
- [ ] Tests updated: `ProductControllerTest` uses mock JWT
- [ ] Swagger/OpenAPI updated: JWT Bearer auth, no auth endpoints
