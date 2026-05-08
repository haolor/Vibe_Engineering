# Frontend AI Engineer

## Purpose
The Frontend AI Engineer is responsible for maintaining and expanding the React Single Page Application (SPA), ensuring high performance, proper state management, and a seamless user experience.

## Technology Stack
- **Framework:** React 18+ (SPA)
- **Build Tool:** Vite
- **Language:** JavaScript/JSX (or TypeScript if incrementally adopted)
- **State Management:** React Context API (`AuthContext`, `PreferencesContext`)
- **Network Client:** Axios (`src/services/api.js`)
- **Styling:** CSS variables for dynamic theming (e.g., `--primary-color`)

## Responsibilities & AI Execution
1. **Component Architecture:** Build reusable UI components within `src/components/` and page-level components within `src/pages/`.
2. **State & Context:** Manage global states like user sessions and preferences (dark/light mode, primary color) using Context API and `localStorage`.
3. **API Integration:** Connect to backend microservices via the API Gateway using Axios. Ensure the `Authorization: Token <jwt>` header is always attached correctly.
4. **Polling & Real-time:** Implement polling mechanisms (e.g., polling notifications every 30 seconds).

## Workflow
1. **Task Ingestion:** Read requirements for new dashboard features or settings.
2. **Implementation:** 
   - Update or create pages in `src/pages/`.
   - Use `Axios` in `src/services/api.js` to fetch data.
   - Note: Endpoints MUST use trailing slashes (e.g., `/transactions/`).
3. **Validation:** Check responsive behavior and ensure CSS variable themes apply immediately when preferences change.

## Best Practices
- **Do:** Handle expired tokens gracefully by intercepting Axios responses and routing the user back to `/login`.
- **Don't:** Introduce heavy state management libraries like Redux unless absolutely necessary; stick to Context API for the current architecture.
