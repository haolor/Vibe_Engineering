# Deploy Command Workflow

## Purpose
Standardizes the automated deployment process to ensure zero-downtime, secure, and reliable releases to Staging and Production environments.

## Infrastructure Stack
- **Containerization:** Docker, Docker Compose.
- **Orchestration:** Kubernetes (for complex microservices) or AWS ECS.
- **Frontend Hosting:** Nginx / Static CDN (served from `static/frontend`).
- **CI/CD Pipeline:** GitHub Actions.

## Step-by-Step Workflow

### 1. Build & Validation (Continuous Integration)
- Code is merged to `main` or `release` branch.
- CI server triggers.
- Run Linters (`npm run lint`), Type Check (`tsc --noEmit`), and Test Suites (`npm run test:all`).
- *AI Responsibility:* Analyze CI logs. If failure occurs, halt deployment and notify Dev Agent.

### 2. Artifact Generation
- Build production bundles (e.g., `npm run build` in frontend).
- Build Docker images and tag them with the Git commit SHA (`image:tag-sha123`).
- Push images to Elastic Container Registry (ECR) or Docker Hub.

### 3. Deployment (Continuous Deployment)
- **Frontend:** Serve the built `dist` folder statically via Nginx or the Spring Boot static resource folder.
- **Backend:** Update Docker Compose or Kubernetes Deployments with the new Spring Boot image tag.
- Apply pending database migrations. *Caution:* Ensure migrations are backwards compatible.

### 4. Verification & Monitoring
- Run Post-deployment Smoke Tests (Ping health-check endpoints).
- Monitor Sentry and Datadog for 15 minutes post-deploy for anomaly detection (spike in 500 errors).

### 5. Rollback Strategy
- If Smoke Tests fail or error rates spike > 5%, immediately revert Kubernetes deployment to the previous Git SHA image tag.
- *AI Responsibility:* Auto-trigger rollback workflow and output Root Cause Analysis of the failure.
