# Review Command Workflow

## Purpose
Enforces rigorous code quality, security, and architectural standards before any code is merged into the main branch. Operates as an automated gatekeeper.

## Code Review Checklist

### 1. Architectural Alignment
- Does the code follow the conventions in `[rules/project-structure.md](../rules/project-structure.md)`?
- Are business logic layers bleeding into UI/Controllers?
- Are new dependencies introduced? If so, are they necessary and audited?

### 2. Clean Code & Readability
- Are variables and functions named expressively (`[rules/clean-code.md](../rules/clean-code.md)`)?
- Is the code DRY? Have large functions been broken down into smaller, testable units?
- Are complex regexes or algorithms accompanied by explanatory comments?

### 3. Performance & Scalability
- Are there N+1 query problems in the ORM calls?
- Are large lists in the frontend virtualized? Are images lazy-loaded?
- Are expensive computations memoized (`useMemo`, Redis cache)?

### 4. Security Check
- Are inputs validated using Zod/Joi?
- Is there any risk of SQL injection (e.g., using raw strings instead of parameterized queries)?
- Are secrets/API keys hardcoded?

### 5. Test Coverage
- Do the new features have accompanying unit tests?
- Did the overall test coverage drop? (Fail the PR if coverage drops below 80%).

## AI Responsibilities
- **Auto Code Review:** The AI Review Agent will parse the `git diff`, run static analysis tools, and add inline comments on the PR highlighting violations of the above checklist.
- **Suggest Refactor:** Provide concrete code snippets to improve anti-patterns detected during the review.
- **Approval:** Only approve the PR if all checks pass and tests are green.
