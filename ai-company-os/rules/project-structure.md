# Project Structure Rules

## Purpose
Standardize the repository layout. We utilize a polyglot repository containing a React Vite frontend and a Spring Boot microservices backend.

## Root Structure
```text
project-root/
├── frontend/              # Vite React SPA
│   ├── src/
│   │   ├── components/    # Reusable UI parts
│   │   ├── contexts/      # AuthContext, PreferencesContext
│   │   ├── pages/         # Dashboard, Transactions, Settings
│   │   └── services/      # api.js (Axios instances)
│   └── vite.config.js
├── backend/               # Spring Boot Ecosystem
│   ├── eureka-server/     # Service Registry
│   ├── api-gateway/       # JWT Filter & Routing
│   ├── auth-service/      # Authentication & User Preferences
│   ├── finance-service/   # Transactions & Statistics
│   ├── notification-service/
│   ├── scripts/           # PowerShell smoke tests
│   ├── docker-compose.yml # Local infrastructure
│   └── pom.xml            # Parent Maven POM
├── docs/                  # Original project docs
├── static/frontend/       # Compiled production frontend
└── ai-company-os/         # THIS AI Operating System
```

## Module / Folder Naming Conventions
- Java Packages: `com.vibe.auth`, `com.vibe.finance`, etc.
- React Components: `PascalCase.jsx` (e.g., `Dashboard.jsx`).
- Backend Modules: Follow standard Spring structure (`controller`, `service`, `repository`, `model`, `dto`, `config`).

## Execution Boundaries
- Frontend developers MUST NOT write backend Java code.
- Backend developers MUST NOT write React code.
- The `api-gateway` is the only bridge between the two domains.
