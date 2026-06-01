# Implementation Workflow

Objective

Implement a single task from specs/tasks/.

Input

- AGENTS.md (code style, architecture rules)
- specs/ (architecture, services, events, database, frontend, decisions)
- specs/tasks/<selected-task>
- docs/ (methodology reference)

Output

- source code
- tests (unit + integration)
- documentation updates (if required)
- API collection (`docs/api-collection.json`) when endpoints change
- OpenAPI/Swagger spec auto-generated at runtime

Steps

1. Read AGENTS.md for code style and project rules.
2. Read relevant specifications (specs/) and docs/ for context.
3. Read the selected task and its acceptance criteria.
4. Set task Status to `IN_PROGRESS`.
5. Implement the task following project conventions:
   - Java 21, Spring Boot 3, constructor injection
   - Records for DTOs, Lombok only when useful
   - Clean package structure
6. Create tests (JUnit 5, Mockito, Testcontainers when needed).
7. Run tests and verify all acceptance criteria pass.
8. Update documentation if the change affects APIs, events, or configuration.
9. If the task adds or modifies HTTP endpoints, add OpenAPI annotations (`@Operation`, `@ApiResponse`, etc.) to controllers and DTOs.
10. If the task adds or modifies HTTP endpoints, generate or update `docs/api-collection.json` (Postman Collection v2.1 format) with all service endpoints.
11. Verify Swagger UI renders correctly at `/swagger-ui/index.html` for the affected service(s).
12. Set task Status to `DONE`.

Rules

- Implement only the requested task.
- Do not implement future tasks or scope-creep.
- Follow coding conventions from AGENTS.md.
- Tests are mandatory for all implementation tasks.

Definition of Done

A task is complete only when:

- Code is implemented and follows project conventions.
- Tests are implemented and pass.
- Acceptance criteria are satisfied.
- Relevant documentation is updated.
- API collection (`docs/api-collection.json`) is up to date when endpoints exist or changed.
- OpenAPI/Swagger spec is auto-generated and accessible at `/swagger-ui/index.html`.
- Task status is marked complete.
