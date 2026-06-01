# ADR-006: Flyway for Database Migrations

## Status

Accepted

## Decision

Use Flyway for database schema migrations with Hibernate `ddl-auto: validate`.

## Context

Each service manages its own database schema. Schema changes must be versioned, repeatable, and applied automatically on service startup.

## Options Considered

1. **Flyway** — Versioned SQL migrations, Spring Boot auto-configuration, widely used.
2. **Liquibase** — More complex, XML/YAML/JSON changelogs. Powerful but more verbose for simple schemas.
3. **Hibernate ddl-auto: update** — Automatic schema generation. No versioning, dangerous in production.
4. **Manual SQL scripts** — No automation, error-prone.

## Rationale

- Flyway provides versioned, ordered migrations that are applied exactly once
- `ddl-auto: validate` ensures the JPA entities match the Flyway-managed schema
- SQL-based migrations are easy to write and review
- Spring Boot auto-configures Flyway with zero additional setup
- Failed migrations block startup, preventing silent schema drift
- Rollback scripts can be added when needed

## Consequences

- Migrations must be immutable once applied (no editing existing migration files)
- Each service has its own migration directory under `src/main/resources/db/migration/`
- Naming convention: `V<version>__<description>.sql`
- Hibernate validation may fail if entities don't match the schema
- Clean database = apply all migrations from scratch

## Related

- specs/tech-specs/tech-specs.md (Flyway Migrations section)
- specs/database/database-specs.md
