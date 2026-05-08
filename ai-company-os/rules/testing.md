# Testing Rules

## Purpose
Automated testing is mandatory to ensure reliability across the React frontend and Spring Boot microservices backend.

## Backend Testing (Java/Spring Boot)
1. **Unit Tests:** Use **JUnit 5** and **Mockito**. Test `@Service` business logic in isolation by mocking the `@Repository` layer.
2. **Integration Tests:** Use `@SpringBootTest` and `Testcontainers` (MongoDB module) to test database interactions.
3. **Smoke Tests:** Use the existing PowerShell script (`backend/scripts/smoke-test.ps1`) to verify the E2E flow (register, login, create transaction, get stats) against the running Docker containers.

## Frontend Testing (React)
1. **Unit Tests:** Use **Jest** and **React Testing Library (RTL)** to test individual UI components.
2. **Context Tests:** Mock the `Axios` API responses to test how `AuthContext` and `PreferencesContext` update global state.
3. **E2E Tests:** (Optional/Future Upgrade) Cypress or Playwright can be introduced to simulate complete user journeys clicking through the Vite SPA.

## Coverage Rules
- All new Spring Boot controllers and services must have accompanying unit tests.
- PRs that break the `smoke-test.ps1` script will be instantly rejected.

## Mocking Strategy
- Backend: Never hit a live external database during unit testing. Use Mockito.
- Frontend: Never hit the real backend during component testing. Mock Axios.
