# Task Generation Workflow

Objective

Transform specifications into small, executable tasks with acceptance criteria.

Input

- specs/ (prd, architecture, services, tech-specs, events, database, frontend, decisions)

Output

specs/tasks/          # One file per task
├── task-001-name.md
├── task-002-name.md
└── ...

Steps

1. Read all specifications in specs/.
2. Decompose specifications into the smallest independently implementable units.
3. Identify and document implementation dependencies between tasks.
4. Create one task file per unit with clear acceptance criteria.
5. Order tasks by dependency (dependencies first).
6. Name task files with a zero-padded sequence number (e.g., task-001-*, task-002-*).

Task Format

```markdown
# Task: <title>

Status: TODO

Dependencies: <task-ids>

## Description

<what needs to be done>

## Acceptance Criteria

- [ ] <criterion 1>
- [ ] <criterion 2>

## Implementation Notes

<optional guidance from specs>
```

Status Values

- `TODO` — not started
- `IN_PROGRESS` — being implemented
- `DONE` — implementation complete, tests pass, docs updated
- `BLOCKED` — waiting on dependency or external factor

Rules

- One task per logical change.
- Tasks must be independently executable.
- Acceptance criteria must be testable.
- Do not skip dependency ordering.
- Do not generate implementation code beyond task definitions.
