# Review Workflow

Objective

Validate implementation against specifications and project standards.

Input

- source code (implemented changes)
- specs/ (prd, architecture, services, tech-specs, events, database, frontend, decisions)
- specs/tasks/<implemented-task>
- docs/ (methodology reference)

Output

specs/reviews/<task-id>-review.md

Steps

1. Validate architecture compliance — does the code follow the architecture specs?
2. Validate coding standards — does the code follow AGENTS.md conventions?
3. Validate RabbitMQ contracts — are events, queues, and exchanges correctly defined?
4. Validate security — are endpoints secured with JWT? Are passwords BCrypt-encoded?
5. Validate tests — do tests exist? Do they cover acceptance criteria? Do they pass?
6. Validate acceptance criteria — does the implementation satisfy all criteria from the task?
7. Validate API collection — if the task introduces or changes endpoints, verify `docs/api-collection.json` is updated and reflects the current API surface.
8. Validate Swagger — verify Swagger UI (`/swagger-ui/index.html`) loads and documents the new or changed endpoints with correct request/response schemas.
9. Generate review report.

Review Report Format

```markdown
# Review: <task-title>

## Findings

- [x] Architecture compliance
- [ ] Coding standards (issue: <details>)
- [ ] RabbitMQ contracts
- [ ] Security
- [ ] Tests and coverage
- [ ] API collection updated
- [ ] Swagger UI reflects current API surface

## Issues

1. <file>:<line> — <description> (severity: high/medium/low)

## Recommendations

- <suggestion>

## Verdict

PASS / FAIL (with issues)
```

Rules

- Report issues with file paths and line numbers.
- Suggest improvements, do not implement them unless requested.
- Be objective and reference specs explicitly.
- Do not modify code during review.
