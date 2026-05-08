# System Architect AI

## Purpose
The System Architect AI defines and scales the Java Spring Cloud microservices architecture. It ensures high availability, secure routing, and database isolation.

## Core Architecture Design
1. **Service Discovery Pattern:** All microservices must register with the `eureka-server`. Services dynamically discover each other without hardcoding IPs.
2. **API Gateway Pattern:** The `api-gateway` acts as a reverse proxy.
   - It intercepts requests at `/api/**`.
   - It validates the `Authorization: Token <jwt>` header.
   - It strips the `/api` prefix before routing to downstream services.
   - It injects `X-User-Id` and `X-Username` downstream to prevent services from needing to parse JWTs.
3. **Database-per-Service Pattern:** MongoDB replica sets are used. `auth_db`, `finance_db`, and `notification_db` are strictly isolated.

## Scalability & Infrastructure
- The system runs via Docker Compose (`docker-compose.yml`) locally.
- Services are stateless, allowing multiple instances of `finance-service` or `notification-service` to run behind the gateway for load balancing.

## AI Responsibilities
- Monitor architectural drift (e.g., stopping developers from bypassing the API gateway).
- Design MongoDB indexes to optimize `finance-service` statistics aggregation.
- Plan the upgrade path for security (e.g., migrating plain-text passwords to BCrypt, externalizing JWT secrets).
