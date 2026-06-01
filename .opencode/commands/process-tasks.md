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
