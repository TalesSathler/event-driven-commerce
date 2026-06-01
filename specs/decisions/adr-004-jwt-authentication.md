# ADR-004: JWT Authentication with Spring Security

## Status

Accepted

## Decision

Use JWT (JSON Web Tokens) with Spring Security and BCrypt password encoding for authentication and authorization.

## Context

The platform requires user authentication to protect product management endpoints. The solution must be stateless (no server-side sessions) to align with microservices architecture.

## Options Considered

1. **JWT + Spring Security** — Stateless, industry standard, well-supported in Spring ecosystem.
2. **Session-based auth** — Requires session affinity or centralized session store. Not ideal for microservices.
3. **OAuth2 / Keycloak** — Full-featured but over-engineered for current scope. Can be added later.
4. **API Keys** — Simpler but less secure, no user identity granularity.

## Rationale

- JWT is stateless by design — no server-side session storage needed
- Spring Security provides first-class JWT support via `SecurityFilterChain`
- HMAC-SHA512 signing provides strong integrity protection
- 24-hour token expiration balances security and user experience
- BCrypt is the industry standard for password hashing
- Future services can validate the same JWT without shared session storage

## Consequences

- JWT secret must be securely managed (stored in application.yml, not hardcoded in production)
- Token revocation is not possible without a blacklist (accept for current scope)
- JWT size grows with claims — keep claims minimal (userId, email, iat, exp)
- All protected endpoints must extract and validate the token from the Authorization header

## Implementation

- `JwtUtil` — Token generation (`generateToken`) and validation (`validateToken`)
- `JwtAuthFilter` — Extracts Bearer token from request, validates, sets SecurityContext
- `SecurityConfig` — Permits unauthenticated access to `/api/auth/**`, requires JWT for all other paths
- `PasswordEncoder` — BCrypt bean in SecurityConfig

## Related

- specs/services/product-service.md (Security section)
- specs/tech-specs/tech-specs.md (JWT Configuration section)
- ADR-007 (supersedes signing algorithm — RS256 replaces HMAC-SHA512)
