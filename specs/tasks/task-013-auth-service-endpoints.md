# Task: Auth Service — Registration, Login, and Token Endpoints

Status: DONE

Dependencies: task-012-auth-service-project, task-014-rsa-key-management

## Description

Implement the authentication REST endpoints: register, login, and refresh token. Use BCrypt for password hashing and RS256 for JWT signing.

Refer to specs/services/auth-service.md for endpoint contracts.

## Acceptance Criteria

- [ ] `POST /api/auth/register` creates user, returns JWT + refresh token (201)
- [ ] `POST /api/auth/login` validates credentials, returns JWT + refresh token (200)
- [ ] `POST /api/auth/refresh` accepts valid refresh token, returns new JWT (200)
- [ ] Email uniqueness is enforced (409 on duplicate)
- [ ] Invalid credentials return 401
- [ ] Passwords are BCrypt-encoded
- [ ] Validation: name @NotBlank, email @Email, password @Size(min=6)
- [ ] `AuthController.java` with register, login, refresh endpoints
- [ ] `AuthService.java` with business logic
- [ ] `RegisterRequest.java`, `LoginRequest.java`, `RefreshRequest.java` DTOs
- [ ] `AuthResponse.java`, `TokenResponse.java` response DTOs
- [ ] Tests: `AuthControllerTest`, `AuthServiceTest`
