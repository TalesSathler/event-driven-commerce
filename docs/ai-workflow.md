# How to Use AI Workflows

## Purpose

This document explains how to use OpenCode with the project's Spec-Driven Development (SDD) workflow.

For a detailed explanation of the SDD methodology, specification types, and project structure, see [spec-driven-development.md](spec-driven-development.md).

The AI should not start coding immediately.

Every new feature must follow the workflow:

```text
Idea
 ↓
Planning
 ↓
Refinement
 ↓
Task Generation
 ↓
Implementation
 ↓
Review
```

---

# How to Provide Feature Input

The human starts each workflow phase by describing what they want. The AI then executes the workflow based on that input.

Examples of human input:

```
I want to add a product catalog with categories, search, and pagination.
```
```
Create an inventory service that syncs stock when products are created.
```
```
Implement task 003 - Configure Flyway migrations for the product service.
```

Each step below shows a template prompt with `[HUMAN INPUT]` where the user fills in their request.

---

# Available Workflows

```text
workflows/
├── planning.md
├── refinement.md
├── task-generation.md
├── implementation.md
└── review.md
```

Each workflow has a specific purpose.

---

# Specification Structure

All specifications live under `specs/`. The directory layout follows the SDD methodology:

```text
specs/
├── architecture/   - System architecture, service interactions, infrastructure
├── services/       - Service responsibilities, APIs, business rules, security
├── events/         - Event contracts, producers, consumers, payloads
├── database/       - Database ownership, entities, relationships
├── frontend/       - Pages, user flows, API integrations, UI requirements
└── decisions/      - Architecture Decision Records (ADRs)
```

See [spec-driven-development.md](spec-driven-development.md) for detailed examples of each type.

---

# Step 1 - Planning

Use when:

* Starting a new project
* Creating a new feature
* Creating a new service

Human provides: feature description, goals, constraints.

Prompt template:

```text
Read AGENTS.md.

Read docs/ai-workflow.md.

Read workflows/planning.md.

[HUMAN INPUT: Describe the feature, goals, and constraints here]

Execute the workflow.
```

Example:

```text
Read AGENTS.md.

Read docs/ai-workflow.md.

Read workflows/planning.md.

I want to add a product catalog service with CRUD operations,
JWT authentication, and publish ProductCreated events to RabbitMQ.

Execute the workflow.
```

Expected output:

```text
specs/architecture/
specs/services/
specs/events/
specs/database/
specs/frontend/
specs/decisions/
```

No implementation code should be generated.

---

# Step 2 - Refinement

Use when:

* Specifications are incomplete
* Requirements changed
* Architecture needs review

Human provides: optionally, specific concerns or updated requirements.

Prompt template:

```text
Read AGENTS.md.

Read specs/.

Read workflows/refinement.md.

[HUMAN INPUT: Optional concerns, updated requirements, or areas to focus on]

Execute the workflow.
```

Example:

```text
Read AGENTS.md.

Read specs/.

Read workflows/refinement.md.

Check that inventory sync includes quantity and reorder threshold fields.

Execute the workflow.
```

Expected output:

* Improved specifications
* Missing requirements identified
* Missing APIs identified
* Missing event contracts identified
* Missing database changes identified

No implementation code should be generated.

---

# Step 3 - Task Generation

Use when:

* Specifications are approved by the human
* Development is ready to start

Human provides: confirmation that specs are ready, optionally a preferred starting point.

Prompt template:

```text
Read AGENTS.md.

Read specs/.

Read workflows/task-generation.md.

[HUMAN INPUT: Confirm specs are approved. Optionally specify priorities.]

Execute the workflow.
```

Example:

```text
Read AGENTS.md.

Read specs/.

Read workflows/task-generation.md.

The specifications look good. Generate tasks for the product service.
Prioritize infrastructure setup first (database, RabbitMQ config).

Execute the workflow.
```

Expected output:

```text
specs/tasks/
```

Example:

```text
Task 001 - Create Product Entity
Task 002 - Configure PostgreSQL
Task 003 - Configure Flyway
Task 004 - Configure JWT
```

---

# Step 4 - Implementation

Use when:

* A specific task must be implemented

Human provides: the task ID or title to implement.

Prompt template:

```text
Read AGENTS.md.

Read specs/.

Read workflows/implementation.md.

[HUMAN INPUT: Specify the task to implement, e.g. "Implement task 002"]

Execute the workflow.
```

Example:

```text
Read AGENTS.md.

Read specs/.

Read workflows/implementation.md.

Implement task 002 - Configure PostgreSQL for the product service.

Execute the workflow.
```

Expected output:

* Source code
* Tests
* Documentation updates

Rules:

* Implement only the requested task.
* Do not implement future tasks.
* Follow specifications.

---

# Step 5 - Review

Use after implementation.

Human provides: the task that was implemented.

Prompt template:

```text
Read AGENTS.md.

Read specs/.

Read workflows/review.md.

[HUMAN INPUT: Specify which task's implementation to review]

Execute the workflow.
```

Example:

```text
Read AGENTS.md.

Read specs/.

Read workflows/review.md.

Review the implementation of task 002 - Configure PostgreSQL.

Execute the workflow.
```

Expected output:

* Architecture review
* Code review
* Security review
* Test coverage review

Example findings:

```text
- Missing validation annotations
- Missing RabbitMQ error handling
- Missing integration tests
```

---

# Recommended Daily Workflow

## New Feature

```text
Planning
 ↓
Refinement
 ↓
Task Generation
```

---

## Development

```text
Implement One Task
 ↓
Review
 ↓
Mark Task as DONE
 ↓
Implement Next Task
```

---

# Task Status Management

Tasks should contain a status field.

Example:

```md
# Task 001 - Create Product Entity

Status: TODO

Acceptance Criteria:

- Product entity exists
- JPA annotations configured
- Unit tests created
```

Possible values:

```text
TODO
IN_PROGRESS
DONE
BLOCKED
```

When implementation is finished:

```md
Status: DONE
```

---

# Example Session

Full walkthrough showing human input at each phase.

## Create Specifications

Human:

```text
I want to add a product catalog service with CRUD, JWT auth,
and ProductCreated events.
```

AI reads AGENTS.md, docs/ai-workflow.md, and workflows/planning.md, then executes the planning workflow.

Output: specs are created under specs/architecture/, specs/services/, etc.

---

## Refine

Human:

```text
Check that the product events include price and stock fields.
```

AI reads specs/, docs/, and workflows/refinement.md, then updates specs.

---

## Generate Tasks

Human:

```text
The specs look complete. Generate tasks for the product service.
```

AI reads specs/ and workflows/task-generation.md, then creates task files under specs/tasks/.

---

## Implement Task

Human:

```text
Implement task 002 - Configure PostgreSQL for the product service.
```

AI reads AGENTS.md, specs/, and workflows/implementation.md, then implements the task.

Output: source code, tests, documentation.
Task status is set to DONE.

---

## Review

Human:

```text
Review the implementation of task 002.
```

AI reads specs/, workflows/review.md, and the implementation, then generates a review report under specs/reviews/.

---

# Rules

Always follow this priority order:

```text
1. AGENTS.md
2. specs/
3. workflows/
4. docs/
5. source code
```

If a conflict exists:

```text
AGENTS.md wins.
```

---

# Definition of Done

A task is considered complete only when:

* Code is implemented
* Tests are implemented
* Tests pass
* Documentation is updated
* Task status is set to DONE

---

# SDD Principles

The project follows these principles from the [Spec-Driven Development](spec-driven-development.md) methodology:

1. **Specification First** - Write specs before code
2. **Documentation as Code** - Treat docs as source code
3. **Architecture Before Implementation** - Design before building
4. **Explicit Event Contracts** - Define every event contract upfront
5. **Database Ownership per Service** - Each service owns its data
6. **Incremental Evolution** - Improve iteratively
7. **Traceable Decisions via ADRs** - Document every architectural decision

---

# Goal

The goal is to use OpenCode as:

* Product Manager
* Software Architect
* Tech Lead
* Developer
* Reviewer

while maintaining a consistent Spec-Driven Development process throughout the entire project lifecycle.
