# Clean Code Rules

## Purpose
Enforce principles that ensure the codebase is readable, maintainable, and scalable for humans and AI agents. Code is read ten times more than it is written.

## Core Principles
1. **SOLID Principles:**
   - **Single Responsibility (SRP):** A class/module should have one, and only one, reason to change.
   - **Open/Closed (OCP):** Software entities should be open for extension, but closed for modification.
   - **Liskov Substitution (LSP):** Subtypes must be substitutable for their base types.
   - **Interface Segregation (ISP):** Clients should not be forced to depend on interfaces they do not use.
   - **Dependency Inversion (DIP):** Depend on abstractions, not concretions.
2. **DRY (Don't Repeat Yourself):** Abstract duplicated logic into shared utilities or hooks.
3. **KISS (Keep It Simple, Stupid):** Avoid clever, overly complex solutions. Prefer readability over minor performance gains unless proven a bottleneck.
4. **YAGNI (You Aren't Gonna Need It):** Don't over-engineer for future edge cases that may never happen. Implement what is needed *now*.

## Naming Conventions
- **Intention-Revealing Names:** A variable name should answer: *why it exists, what it does, and how it is used.*
- **Avoid Disinformation:** Do not name a List of accounts `accountList` unless it is strictly a List type. `accounts` is better.
- **Pronounceable Names:** Avoid `genymdhms` (generate year, month, day...). Use `generationTimestamp`.
- **Searchable Names:** Single-letter variables are ONLY acceptable as iterators in short loops. Use `MAX_CLASSES_PER_STUDENT` instead of `7`.
- **Classes/Types:** `PascalCase`.
- **Variables/Functions:** `camelCase`.
- **Constants:** `UPPER_SNAKE_CASE`.

## Modularity & Separation of Concerns
- Never mix business logic with UI components.
- Use the **Controller -> Service -> Repository** pattern in the backend.
- Use **Custom Hooks** to extract complex state logic from React components in the frontend.
