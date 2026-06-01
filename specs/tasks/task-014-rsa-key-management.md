# Task: Auth Service — RSA Key Management and Public Key Endpoint

Status: DONE

Dependencies: task-012-auth-service-project

## Description

Implement RS256 key pair generation, persistent storage, and a public key endpoint. The private key stays exclusively in auth-service; the public key is exposed for downstream services.

Refer to ADR-007 for design decisions.

## Acceptance Criteria

- [ ] `RsaKeyConfig.java` generates 2048-bit RSA key pair on startup
- [ ] Key pair is persisted to disk and reloaded on restart (no key regeneration)
- [ ] `JwtUtil.java` signs tokens with RS256 using private key
- [ ] `JwtUtil.java` validates token signature, expiration, issuer, audience
- [ ] Public key exposed at `GET /api/auth/public-key` (PEM format)
- [ ] Private key is never logged or exposed via API
- [ ] Tests: `JwtUtilTest` (sign + verify round-trip)
