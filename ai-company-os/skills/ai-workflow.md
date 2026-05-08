# Skill: AI Workflow Orchestration

## Concepts
AI Workflow Orchestration is the process of chaining multiple specialized AI agents together to complete a complex software engineering task from idea to production, mimicking a human software company.

## Best Practices
- **Divide and Conquer:** Never ask one model to design the architecture, write the backend, and build the frontend in one prompt. Split the tasks by Agent roles.
- **Artifact Passing:** The output of one AI becomes the input (context) for the next. (e.g., PM Agent outputs a Markdown User Story -> Frontend Agent uses it to write React SPA code).
- **Human-in-the-Loop (HITL):** Humans act as the final reviewers (Tech Leads) to approve PRs and verify AI-generated architecture.

## Workflow
1. **Planning (GPT-4o/Claude 3.5 Sonnet):** Ask the PM Agent to create a User Story.
2. **Architecture (GPT-4o):** Pass the User Story to the System Architect Agent to define the DB Schema.
3. **Scaffolding (Cursor AI):** Use Cursor's Composer to generate the boilerplate files based on the Schema.
4. **Implementation (Claude 3.5 Sonnet):** Generate the actual business logic (Spring Boot) and UI components (React).
5. **Testing (Claude 3.5 Sonnet):** Ask the QA Agent to write JUnit/Mockito specs for the new logic.

## Examples
*Scenario: Adding a Notification System.*
1. **Human -> PM AI:** "We need user notifications."
2. **PM AI -> Architect AI:** "Here is the Epic. Design the DB."
3. **Architect AI -> DB Schema:** Adds `notification_db` and MongoDB schema.
4. **Backend AI:** Writes the `notification-service` in Java.
5. **Frontend AI:** Builds the polling bell icon in React.

## AI Usage Strategy
- Route complex reasoning and planning to **GPT-5.5 / GPT-4o**.
- Route high-volume code generation and refactoring to **Claude 3.5 Sonnet**.
- Route log analysis and long document reading to **Gemini 1.5 Pro**.

## Common Mistakes
- Expecting zero-shot perfection on massive features.
- Failing to provide the AI with the project's existing coding standards (resulting in inconsistent code).

## Optimization Techniques
- Automate the passing of artifacts using scripts or specialized CI/CD pipelines (e.g., GitHub Actions that trigger an AI review script on every PR).
