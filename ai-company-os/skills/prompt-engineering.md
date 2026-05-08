# Skill: Prompt Engineering

## Concepts
Prompt Engineering is the art and science of communicating effectively with Large Language Models (LLMs) to produce accurate, high-quality, and predictable outputs. It involves structuring context, constraints, and instructions.

## Best Practices
- **Assign a Role:** Always tell the AI who it is (e.g., "You are an Expert Next.js Developer...").
- **Provide Context:** Give the AI the "Why". Paste relevant interface definitions or schema files.
- **Be Explicit with Constraints:** Define what the AI should *not* do (e.g., "Do not use `any`. Do not write CSS, use Tailwind only.").
- **Few-Shot Prompting:** Provide 1-2 examples of the desired input/output format before asking the AI to generate the real response.
- **Chain of Thought (CoT):** Ask the AI to "Think step-by-step" before generating code. This forces the model to reason through the problem, reducing logical errors.

## Workflow
1. **Define the Goal:** What exact code or text do you need?
2. **Draft the Prompt:** Write the role, context, task, and constraints.
3. **Execute & Evaluate:** Send to the model. Did it hallucinate? Did it miss a constraint?
4. **Refine:** If the output is wrong, don't just say "fix it". Explain *why* it's wrong and adjust the prompt.

## Examples
**Bad Prompt:** "Write a login component."
**Good Prompt:** "You are a Senior Frontend Engineer. Create a React `LoginForm` component. It must use Context API for state management. Do not use external UI libraries, use raw CSS variables for styling. Return only the `.jsx` code."

## AI Usage Strategy
- **Cursor AI:** Use the `@` symbol to tag specific files or documentation so the model has exact context.
- **System Prompts:** Embed the `[agents/](../agents/)` and `[rules/](../rules/)` markdown files from this repository into the AI's custom instructions/system prompt.

## Common Mistakes
- **Vagueness:** Asking for "a scalable backend" without defining expected traffic or DB type.
- **Context Overload:** Dumping 50 unrelated files into the context window, causing the AI to lose focus (the "Lost in the Middle" phenomenon).

## Optimization Techniques
- Maintain a library of successful prompts in the team wiki.
- Use XML tags (`<context>`, `<task>`) to structure long prompts, helping the LLM parse sections easily.
