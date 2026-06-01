# Refinement Workflow

Objective

Review and improve specifications before task generation.

Input

- specs/ (from Planning phase)
- docs/ (methodology reference)

Output

- Updated specs/ with corrections and additions

Steps

1. Read all specifications in specs/ and relevant docs/ for reference.
2. Identify missing requirements, services, APIs, events, database definitions, frontend flows, or acceptance criteria.
3. Identify inconsistencies between architecture, services, events, database, frontend, and ADR specifications.
4. Update specifications to fill gaps and resolve inconsistencies.
5. Re-validate that specs are complete and consistent across all domains.

Checklist

- Are all functional requirements covered by service and API definitions?
- Are all events defined with contracts (producer, consumer, routing key, payload)?
- Are all data models defined with fields, types, and relationships?
- Is database ownership per service documented?
- Are frontend pages and user flows specified?
- Are acceptance criteria clear and testable?
- Do all architectural decisions have ADRs?
- Are cross-service contracts (RabbitMQ events) documented explicitly?

Rules

- Do not generate implementation code.
- Focus on specification quality.
- Resolve ambiguities before proceeding to task generation.
