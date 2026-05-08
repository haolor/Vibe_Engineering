# System Design Rules

## Purpose
Ensure the architecture handles high traffic and maintains high availability, leveraging the established Spring Cloud microservices ecosystem.

## Microservices Architecture & Communication
- **Service Independence:** Each microservice (`auth-service`, `finance-service`, `notification-service`) must operate independently and be deployable independently.
- **Database per Service:** Avoid the "shared database" anti-pattern. Use isolated MongoDB databases (`auth_db`, `finance_db`, `notification_db`).
- **Synchronous Communication:** If `finance-service` absolutely needs user data from `auth-service`, use `OpenFeign` or `RestTemplate` over HTTP. However, minimize this pattern to avoid tight coupling.
- **Header Injection:** Rely on the `api-gateway` to inject user context (`X-User-Id`) to avoid redundant auth validation calls.

## Database Design (MongoDB)
- **Denormalization for Reads:** Because MongoDB doesn't support efficient cross-collection joins, embed data where necessary. For example, embed `category_name`, `category_icon`, and `category_color` directly inside the Transaction document to ensure dashboard queries are blazing fast.
- **Indexing:** Create single-field and compound indexes on frequently queried fields like `user_id` and `created_at`.

## Caching Strategy
- Currently relies on fast MongoDB document retrieval.
- Future Upgrade: Introduce Redis caching at the `finance-service` layer to cache intensive aggregation pipelines (like `/statistics`).

## Scaling
- Services are stateless. To handle more load, simply spin up more Docker containers of `finance-service`. Eureka Server will automatically load-balance incoming Gateway traffic across all available instances.
