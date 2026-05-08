# AI Software Engineering Operating System (AI-OS)

## Purpose
This repository acts as the central brain and operating system for an AI-first Software Company. It enforces enterprise-grade standards, architecture, and workflows across multiple AI agents. This `ai-company-os` folder is the SINGLE SOURCE OF TRUTH.

## System Architecture & AI Orchestration

### AI Models & Roles
1. **GPT-5.5 / GPT-4o (Reasoning & Architecture):**
   - **Role:** System Architect, Project Manager, Lead Backend.
   - **Responsibility:** High-level system design, database schemas, complex logic breakdown, and sprint planning. Excellent at defining the "Why" and "How".
2. **Claude 3.5 Sonnet / Opus (Coding & Long Context):**
   - **Role:** Frontend Engineer, UI/UX Designer, QA Automation, Refactoring Lead.
   - **Responsibility:** Ingesting huge documentation, generating clean React (Vite) SPA components, writing tests, and maintaining design systems.
3. **Gemini 1.5 Pro (Multimodal & Log Analysis):**
   - **Role:** Debugger, UI Analyzer.
   - **Responsibility:** Analyzing screenshots for UI/UX heuristic evaluation, processing massive error logs, and tracing distributed system bottlenecks.
4. **Local LLM (Security & Privacy):**
   - **Role:** Security Scanner, Code Reviewer.
   - **Responsibility:** Offline scanning of API keys, PII data, and OWASP vulnerabilities without sending sensitive data over the internet.
5. **Cursor AI / GitHub Copilot (Pair Programming):**
   - **Role:** Copilot for the Human Engineer.
   - **Responsibility:** Inline autocompletion, fast boilerplate generation, and context-aware file editing.

## AI Workflow (From Idea to Production)
1. **IDEA PHASE (Human + PM Agent):** Human provides the vision. Project Manager AI breaks it down into Epics, User Stories, and Sprint Backlog in Markdown format.
2. **ARCHITECTURE PHASE (Architect Agent):** System Architect AI reads the Epics and generates `[system-design.md](rules/system-design.md)`, DB schema, API contracts, and infrastructure requirements.
3. **DESIGN PHASE (UI/UX Agent):** Designer AI drafts wireframes, defines the Design System, and ensures UX heuristics are met.
4. **IMPLEMENTATION PHASE (Frontend + Backend Agents):**
   - Backend AI scaffolds Spring Boot microservices, writes REST endpoints, and connects to MongoDB.
   - Frontend AI builds React components, integrates Context API/Axios, and binds the UI to APIs.
5. **TESTING PHASE (QA Agent):** QA AI generates JUnit/Mockito tests and React unit tests. Any failing tests are sent back to Dev Agents via `[fix-issue.md](commands/fix-issue.md)` workflow.
6. **REVIEW PHASE (Review Agent):** Security & Code Style rules are enforced via `[review.md](commands/review.md)`. PRs are reviewed automatically.
7. **DEPLOYMENT PHASE (DevOps Agent):** Code is pushed via GitHub Actions to Docker environments via `[deploy.md](commands/deploy.md)`.
8. **MONITORING PHASE:** Sentry & Grafana logs are fed back to Gemini for continuous optimization.

## Directory Structure
- `[/agents/](agents/)`: Definitions, responsibilities, and workflows for each AI persona.
- `[/commands/](commands/)`: Executable operational workflows (deploy, fix, review).
- `[/rules/](rules/)`: Hardcoded enterprise standards (clean code, security, testing).
- `[/skills/](skills/)`: Technical guides and best practices for specific domains.
