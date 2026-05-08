# Fix Issue Command Workflow

## Purpose
A rigorous, test-driven approach to identifying, isolating, and resolving bugs in the system without introducing regressions.

## Step-by-Step Workflow

### 1. Issue Triage & Reproduction
- **Action:** Ingest the bug report, Sentry error log, or user feedback.
- **AI Responsibility:** Parse the stack trace, identify the offending file/function, and reliably reproduce the bug in a local/test environment.

### 2. Root Cause Analysis (RCA)
- **Action:** Investigate *why* the bug occurred (e.g., Race condition, Null pointer, Unhandled promise rejection).
- **AI Responsibility:** Output a brief RCA summary explaining the technical failure.

### 3. Test-First Isolation (TDD)
- **Action:** Write a failing Unit or E2E test that explicitly captures the bug's behavior.
- **Rule:** *You are not allowed to touch the application code until a failing test is written.*

### 4. Implementation (The Patch)
- **Action:** Modify the application code to fix the root cause.
- **AI Responsibility:** Ensure the fix adheres to `[rules/clean-code.md](../rules/clean-code.md)` and doesn't introduce collateral damage.

### 5. Verification & Regression
- **Action:** Run the previously failing test (it must now PASS). Run the *entire* test suite to ensure no existing functionality was broken (Regression Testing).

### 6. Code Review & Merge
- **Action:** Submit PR with title format `fix: [scope] brief description`.
- Include the RCA and link the fixed test in the PR description. Trigger the `[review.md](review.md)` command.
