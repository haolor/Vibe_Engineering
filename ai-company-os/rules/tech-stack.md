# Technology Stack Rules

## Purpose
Defines the strictly approved technologies to be used across the Vibe Engineering enterprise. Standardization ensures the AI Agents write code compatible with the current infrastructure.

## Frontend Stack
- **Framework:** React SPA
- **Build Tool:** Vite
- **Language:** JavaScript (JSX)
- **State Management:** React Context (`AuthContext`, `PreferencesContext`), `localStorage`
- **Network Client:** Axios
- **Styling:** CSS Variables (for dynamic Primary Colors and Dark/Light Theme)

## Backend Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.x / Spring Cloud
- **Service Discovery:** Eureka Server
- **API Gateway:** Spring Cloud Gateway
- **Build Tool:** Maven 3.9+

## Database
- **Engine:** MongoDB (Replica Set `rs0`)
- **Isolation:** Database per microservice (`auth_db`, `finance_db`, `notification_db`).

## DevOps & Infrastructure
- **Containerization:** Docker
- **Orchestration (Local):** Docker Compose
- **Smoke Testing:** PowerShell scripts (`smoke-test.ps1`)

## Rule of Adoption
Do not introduce new frameworks (e.g., do not rewrite the frontend in Next.js or the backend in Node.js) unless explicitly requested by the Human Lead. Maintain and evolve the current stack.
