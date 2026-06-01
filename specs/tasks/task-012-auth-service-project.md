# Task: Auth Service — Project Setup, Entities, and Migrations

Status: DONE

Dependencies: none

## Description

Create the auth-service project with Spring Boot, database entities, and Flyway migrations. This service handles user registration, authentication, and JWT issuance using RS256.

Refer to specs/services/auth-service.md, specs/tech-specs/tech-specs.md, specs/database/database-specs.md.

## Acceptance Criteria

- [ ] `services/auth-service/pom.xml` exists with Spring Boot 3.4.4, Java 21
- [ ] Dependencies: Web, Security, Data JPA, AMQP, Validation, Actuator, PostgreSQL, Flyway, jjwt
- [ ] `com.edc.auth` package structure exists per tech-specs
- [ ] `User.java` entity with fields: id, name, email, password, role, createdAt, updatedAt
- [ ] `UserRepository.java` with findByEmail method
- [ ] `RefreshToken.java` entity with fields: id, userId, token, expiresAt, createdAt
- [ ] `RefreshTokenRepository.java` with findByToken method
- [ ] `V1__create_users_table.sql` Flyway migration
- [ ] `V2__create_refresh_tokens_table.sql` Flyway migration
- [ ] `SecurityConfig.java` permits all on `/api/auth/**`
- [ ] `application.yml` with PostgreSQL config, RabbitMQ config, server port 8081
- [ ] `Dockerfile` with Maven build
- [ ] Tests: `UserRepositoryTest`
