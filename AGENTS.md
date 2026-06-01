# Event Driven Commerce Agent Rules

Development Methodology

This project follows Spec-Driven Development (SDD) through 5 workflow phases.
Each phase has a corresponding file under workflows/ that defines its steps.

Workflow Phases

Phase 1 — Planning (workflows/planning.md)
  Goal: Transform project idea into complete specifications.
  Output: specs/architecture/, specs/services/, specs/events/, specs/database/, specs/frontend/, specs/decisions/

Phase 2 — Refinement (workflows/refinement.md)
  Goal: Review and improve specifications before task generation.
  Output: Updated specs/

Phase 3 — Task Generation (workflows/task-generation.md)
  Goal: Decompose specifications into small, executable tasks.
  Output: specs/tasks/

Phase 4 — Implementation (workflows/implementation.md)
  Goal: Implement a single task with tests and documentation.
  Output: Source code, tests, documentation updates

Phase 5 — Review (workflows/review.md)
  Goal: Validate implementation against specifications and standards.
  Output: specs/reviews/

Never generate code before specifications exist.

Architecture:

- Java 21
- Spring Boot 3
- RabbitMQ
- PostgreSQL
- Angular
- Docker Compose

Development Process:

1. Read specs first
2. Implement second
3. Generate tests
4. Update documentation if required

Microservices Rules:

- Service owns its data
- No direct database sharing
- Communication through RabbitMQ when applicable
- Keep services loosely coupled

Security:

- Spring Security
- JWT Authentication
- BCrypt Password Encoding

Code Style:

- Constructor Injection
- Records for DTOs
- Lombok only when useful
- Clean package structure

Testing:

- JUnit 5
- Mockito
- Testcontainers when needed

Source of Truth

Priority Order:

1. AGENTS.md
2. specs/
3. workflows/
4. docs/
5. source code

If a conflict exists:

AGENTS.md wins.
