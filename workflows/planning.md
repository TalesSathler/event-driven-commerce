# Planning Workflow

Objective

Transform a project idea into complete specifications under specs/.

Input

- docs/ai-workflow.md (project overview, methodology, workflow guidance)
- docs/ (architecture, sdD methodology)

Output

specs/
├── prd/              # Product Requirements Documents
├── architecture/     # System architecture, service interactions, infrastructure
├── services/         # Service responsibilities, APIs, business rules, security
├── tech-specs/       # Technical Specifications (APIs, data models, impl details)
├── events/           # Event contracts, producers, consumers, payloads
├── database/         # Database ownership, entities, relationships
├── frontend/         # Pages, user flows, API integrations, UI requirements
└── decisions/        # Architecture Decision Records (ADRs)

Steps

1. Read docs/ai-workflow.md, AGENTS.md, and docs/ to understand goals, constraints, and methodology.
2. Create Product Requirements Documents covering functional and non-functional requirements.
3. Create Architecture Specifications covering services, communication, and infrastructure.
4. Create Service Specifications covering responsibilities, APIs, business rules, and security.
5. Create Technical Specifications covering APIs, data models, and implementation details.
6. Create Event Specifications covering contracts, producers, consumers, and payloads.
7. Create Database Specifications covering ownership, entities, and relationships.
8. Create Frontend Specifications covering pages, user flows, and API integrations.
9. Create Architecture Decision Records for every significant architectural choice.
10. Validate generated specifications against AGENTS.md and docs/ principles, and docs/ai-workflow.md for SDD methodology.

Rules

- Do not generate implementation code.
- Focus on architecture and planning.
- Follow project constraints (e.g., Java 21, Spring Boot 3, RabbitMQ).
- Every ADR must explain the decision, alternatives considered, and rationale.
- Validate that specs are consistent with each other before proceeding.
- Use docs/spec-driven-development.md as reference for specification format and examples.
