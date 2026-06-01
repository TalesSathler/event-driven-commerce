# ADR-007: Dedicated Auth Service with RS256 JWT

## Status

Accepted

## Decision

Extract authentication into a dedicated `auth-service` and migrate from HMAC-SHA512 (symmetric) to RS256 (asymmetric RSA) for JWT signing.

## Context

The current architecture bundles authentication inside product-service. Other services (inventory, future services) need JWT validation but should not have access to the signing key. A symmetric algorithm requires sharing the same secret across all services, creating a security risk. An asymmetric approach allows services to validate tokens with a public key while the private key stays exclusively in auth-service.

## Options Considered

1. **RS256 (RSA Signature with SHA-256)** — Asymmetric, industry standard. Public key distributed, private key stays in auth-service.
2. **HMAC-SHA512 with shared secret** — Current approach. Requires all services to know the secret. Risk of key leakage.
3. **OAuth2 / Keycloak** — Full-featured but heavy. Can be adopted later if needed.
4. **ES256 (ECDSA)** — Smaller keys, same security level. Less library support in JVM ecosystem.

## Rationale

- RS256 is asymmetric — private key stays in auth-service, only public key is distributed
- Spring Boot Resource Server has built-in RS256 support via `spring.security.oauth2.resourceserver.jwt`
- No runtime calls to auth-service for token validation — each service validates locally
- Reduces blast radius: compromised service can only validate tokens, not forge them
- Standard claims (iss, aud, exp, sub, roles) are service-agnostic

## Consequences

- Auth-service becomes a new deployment with its own database
- Product-service and inventory-service must remove user management code
- Key rotation requires a strategy (short-lived keys, JWKS endpoint)
- API Gateway should be introduced to centralize routing
- Public key is exposed via `GET /api/auth/public-key` for service configuration

## Key Management

- Auth-service generates an RS256 key pair on startup if none exists
- Private key is stored securely (never logged, never exposed via API)
- Public key is available at `GET /api/auth/public-key`
- Services fetch the public key at startup and cache it
- Future: JWKS endpoint for key rotation

## Implementation

- `AuthServiceApplication.java` — Spring Boot entry point
- `SecurityConfig.java` — Permit all on `/api/auth/**`, issue tokens
- `JwtUtil.java` — RS256 sign + verify using `java.security.KeyPair`
- `UserController.java` — Register, login, refresh
- `UserService.java` — Business logic
- `User.java` / `UserRepository.java` — User entity + repository
- `RefreshToken.java` / `RefreshTokenRepository.java` — Refresh token support
- Product Service: Replace HMAC validation with RS256 via Spring Security Resource Server
- Inventory Service: Add RS256 JWT validation via Spring Security Resource Server

## Related

- specs/services/auth-service.md
- specs/tech-specs/tech-specs.md (JWT Configuration section)
- ADR-004 (superseded for signing algorithm)
