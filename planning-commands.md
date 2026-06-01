# Planning System — All Commands

Complete sequence to build the Event-Driven Commerce planning system
using Spec-Driven Development (SDD) with OpenCode CLI.

---

## 0. Prerequisites

```bash
# Start infrastructure
docker compose up -d

# Verify containers are running
docker ps
```

---

## 1. Planning — Create Specifications

```text
Read AGENTS.md.

Read docs/ai-workflow.md.

Read workflows/planning.md.

I want to build an Event-Driven E-Commerce Platform with:
- Product Service (CRUD, JWT auth, publish events)
- Inventory Service (consume product events, manage stock)
- RabbitMQ for async communication
- PostgreSQL per service
- Angular frontend
- Docker Compose for local dev

Execute the workflow.
```

**Output:** `specs/prd/`, `specs/architecture/`, `specs/services/`,
`specs/tech-specs/`, `specs/events/`, `specs/database/`,
`specs/frontend/`, `specs/decisions/`

---

## 2. Refinement — Improve Specifications

```text
Read AGENTS.md.

Read specs/.

Read workflows/refinement.md.

Check that product events include price and stock fields, and that
database ownership per service is clearly documented.

Execute the workflow.
```

**Output:** Updated `specs/` with corrections and additions.

---

## 3. Task Generation — Decompose Into Tasks

```text
Read AGENTS.md.

Read specs/.

Read workflows/task-generation.md.

The specifications look good. Generate tasks for the entire platform.
Prioritize infrastructure setup first (Docker, database, RabbitMQ config).

Execute the workflow.
```

**Output:** `specs/tasks/task-001-*.md` through `specs/tasks/task-NNN-*.md`

---

## 4. Implementation — Build One Task

```text
Read AGENTS.md.

Read specs/.

Read workflows/implementation.md.

Implement task 001 — Configure Docker Compose with PostgreSQL,
RabbitMQ, and service containers.

Execute the workflow.
```

**Output:** Source code, tests, documentation updates.
Task status set to `DONE`.

Repeat for each task:

```text
Read AGENTS.md.

Read specs/.

Read workflows/implementation.md.

Implement task NNN — <task title>.

Execute the workflow.
```

---

## 5. Review — Validate Implementation

```text
Read AGENTS.md.

Read specs/.

Read workflows/review.md.

Review the implementation of task 001 — Configure Docker Compose.

Execute the workflow.
```

**Output:** `specs/reviews/task-001-review.md`

### Commit implementation + review together

```bash
git add -A
git commit -m "feat: task 001 — Configure Docker Compose"
```

---

## 6. Automation — Loop Through All Tasks

### Option A: Custom Command (recommended for TUI)

Create `.opencode/commands/process-tasks.md`:

```markdown
---
description: Implement, review, and commit all pending tasks
agent: build
---

Read AGENTS.md.

Read specs/.

Read workflows/implementation.md.
Read workflows/review.md.

List all tasks in specs/tasks/ sorted by dependency order.
For each task with Status != DONE:
  1. Execute the Implementation workflow for that task.
  2. Execute the Review workflow for that task.
  3. If review passes, run:
     git add -A && git commit -m "feat: task NNN — <title>"
  4. Proceed to the next task.
Stop when all task files have Status: DONE.

Execute.
```

Then run in the TUI:

```
/process-tasks
```

### Option B: Custom Subagent

Create `.opencode/agents/task-runner.md`:

```markdown
---
description: Processes all TODO tasks — implements, reviews, and commits each one
mode: subagent
---

You are a task automation agent. Your only job is:

1. Read specs/tasks/ and find all tasks sorted by dependency order.
2. For each task with Status != DONE:
   a. Read workflows/implementation.md and execute it for that task.
   b. Read workflows/review.md and execute it for that task.
   c. If review passes: git add -A && git commit -m "feat: task NNN — <title>"
3. Stop when all tasks are DONE.
```

Invoke from TUI:

```
@task-runner Process all pending tasks
```

### Option C: Single OpenCode CLI Command

```bash
opencode -p "Read AGENTS.md. Read specs/. Read workflows/implementation.md. Read workflows/review.md. List all tasks in specs/tasks/ sorted by dependency order. For each task with Status != DONE: 1. Execute the Implementation workflow. 2. Execute the Review workflow. 3. If review passes, run: git add -A && git commit -m 'feat: task NNN — <title>'. 4. Proceed to next task. Stop when all tasks are DONE. Execute."
```

### Option D: Shell Script

```bash
#!/usr/bin/env bash
# run-planning.sh — Process all TODO tasks sequentially
set -euo pipefail

TASKS=$(find specs/tasks -name 'task-*.md' | sort)

for task_file in $TASKS; do
  if grep -q "Status: DONE" "$task_file"; then
    echo "SKIP: $task_file already DONE"
    continue
  fi

  task_id=$(echo "$task_file" | grep -oP 'task-\d+')
  title=$(head -1 "$task_file" | sed 's/# Task: //')

  echo "=== $task_id: $title ==="

  opencode -p "Read AGENTS.md. Read specs/. Read workflows/implementation.md.
    Implement $task_id. Execute the workflow."

  opencode -p "Read AGENTS.md. Read specs/. Read workflows/review.md.
    Review the implementation of $task_id. Execute the workflow."

  git add -A && git commit -m "feat: $task_id — $title"
done

echo "All tasks complete!"
```

---

## Quick Reference

| Phase | Action | Commit? |
|-------|--------|---------|
| Planning | Read `workflows/planning.md` → Execute | — |
| Refinement | Read `workflows/refinement.md` → Execute | — |
| Task Gen | Read `workflows/task-generation.md` → Execute | `git commit -m "docs: add tasks for <feature>"` |
| Implement | Read `workflows/implementation.md` → Execute | — |
| Review | Read `workflows/review.md` → Execute | `git commit -m "feat: task NNN — <title>"` |

## Daily Dev Loop

```text
Implement One Task → Review → Commit → Implement Next Task
```

## Task Status Values

- `TODO` — not started
- `IN_PROGRESS` — being implemented
- `DONE` — complete (code + tests + docs)
- `BLOCKED` — waiting on dependency
