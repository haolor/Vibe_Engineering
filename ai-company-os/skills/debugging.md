# Skill: Debugging

## Concepts
Debugging is the systematic process of identifying, isolating, and fixing bugs. In an AI-first workflow, debugging shifts from manual `console.log` hunting to log analysis and automated root-cause detection.

## Best Practices
- **Read the Error:** The answer is almost always in the stack trace.
- **Isolate the Variable:** Remove external factors until the bug is perfectly reproducible in a minimal environment.
- **Test-Driven Debugging (TDD):** Write a failing test that reproduces the bug BEFORE changing any code.

## Workflow
1. **Reproduce:** Confirm the steps to trigger the bug.
2. **Gather Context:** Copy the error log, the network payload, and the relevant code files.
3. **AI Analysis:** Paste the context into an AI model (e.g., Gemini 1.5 Pro for huge logs) with the prompt: "Explain the root cause of this error and suggest a fix."
4. **Implement Fix:** Apply the patch.
5. **Verify:** Run the failing test. It should now pass.

## Examples
*Error:* `Cannot read properties of undefined (reading 'map')`
*Debugging Steps:*
1. Look at the stack trace to find the exact line.
2. Realize the API is returning `null` instead of an empty array `[]`.
3. Fix: Add a fallback `(data?.items || []).map(...)` or fix the backend to always return an array.

## AI Usage Strategy
- Feed Sentry error logs directly into Claude or GPT-4.
- Ask the AI: "Are there any race conditions in this `useEffect`?" or "Why is this database query deadlocking?"
- Use Cursor AI's inline chat to highlight an error squiggly and press `Cmd+K` -> "Fix this".

## Common Mistakes
- "Shotgun Debugging": Randomly changing code and refreshing the page hoping it works.
- Fixing the symptom instead of the root cause (e.g., adding `?` everywhere instead of fixing the corrupted database record).

## Optimization Techniques
- Use Git Bisect to mathematically find the exact commit that introduced the bug.
- Enable `source-maps` in production monitoring to see the exact TypeScript lines instead of minified JS.
