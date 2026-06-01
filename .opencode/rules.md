# AI Rules

## General

Always read:

* AGENTS.md
* docs/ai-workflow.md
* specs/
* workflows/

before making significant changes.

## Development Process

Never start implementation before specifications exist.

Always follow:

1. Planning
2. Refinement
3. Task Generation
4. Implementation
5. Review

## Implementation Rules

Implement only the requested task.

Do not implement future tasks.

Do not invent requirements.

Do not invent APIs.

Do not invent RabbitMQ events.

Follow documented specifications.

## Architecture Rules

Use:

* Java 21
* Spring Boot 3
* PostgreSQL
* RabbitMQ
* Angular
* Docker Compose

## Code Quality

Prefer:

* Constructor Injection
* Records for DTOs
* Clear package organization
* SOLID principles
* Small incremental changes

Avoid:

* Premature optimization
* Unnecessary abstractions
* Complex frameworks

## Testing

Generate tests when implementing features.

Prefer:

* JUnit 5
* Mockito
* Testcontainers

## Documentation

When a feature is completed:

* Update task status
* Update service README if necessary
* Keep documentation synchronized with implementation

## Output Style

Be concise.

Prefer implementation over explanation.

Keep responses focused on the current task.
