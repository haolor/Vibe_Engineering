# Code Style Rules

## Purpose
Ensure a uniform codebase formatting across all repositories. Uniformity reduces cognitive load and allows developers (and AI) to focus on logic rather than formatting.

## Tooling
- **Prettier:** Used for all code formatting (indentation, quotes, trailing commas). 
  - *Rule:* Prettier must run automatically on `git commit` via Husky and lint-staged.
- **ESLint:** Used for catching code quality issues and enforcing strict typing rules.
  - *Rule:* No `@ts-ignore` allowed without an accompanying explanatory comment and a Jira ticket to fix it later.

## Formatting Standards
1. **Indentation:** 2 spaces (no tabs).
2. **Quotes:** Single quotes for strings (`'hello'`), double quotes for JSX attributes (`className="flex"`).
3. **Semicolons:** Always required.
4. **Trailing Commas:** `all` (ES5 and ES6 objects/arrays). This makes git diffs cleaner.
5. **Line Length:** Maximum 100 characters.

## Imports Ordering
Imports must be structured consistently. ESLint plugin `eslint-plugin-simple-import-sort` should enforce this:
1. React / Core framework imports (`import React from 'react';`)
2. Third-party node_modules (`import { max } from 'lodash';`)
3. Absolute internal imports (`import { Button } from '@/components/ui';`)
4. Relative parent imports (`import { helpers } from '../utils';`)
5. Relative sibling imports (`import { styles } from './styles.css';`)

## Comments Policy
- **Explain "Why", not "What":** The code should explain *what* it is doing. Comments should explain *why* it is doing it (business context, weird edge case bug fixes).
- **JSDoc/TSDoc:** Required for public utility functions, complex types, and API endpoint controllers.
  ```typescript
  /**
   * Calculates the discounted price based on the user's tier.
   * @param {number} basePrice - The original price in cents.
   * @param {'GOLD' | 'SILVER'} tier - The user's loyalty tier.
   * @returns {number} The final price in cents.
   */
  ```

## Typing Conventions (TypeScript)
- Never use `any`. Use `unknown` if the type is truly dynamic, then use type narrowing.
- Prefer `interface` over `type` for object shapes as they are more performant for the TS compiler, unless utilizing Unions/Intersections.
