# API Conventions Rules

## Purpose
Ensure all APIs follow the Vibe Engineering standard formats, enabling seamless communication between the React Frontend and Spring Boot Backend.

## Routing & Trailing Slashes
- **CRITICAL RULE:** All REST endpoints MUST have a **trailing slash**. 
  - *Correct:* `/api/auth/login/`
  - *Incorrect:* `/api/auth/login`
- The `api-gateway` receives requests prefixed with `/api` (e.g., `/api/transactions/`), strips the `/api` prefix, and forwards the request to the microservice (e.g., `finance-service/transactions/`).

## Authentication Headers
- The system uses the `Token` keyword, **NOT `Bearer`**.
  - *Correct:* `Authorization: Token <jwt>`
  - *Incorrect:* `Authorization: Bearer <jwt>`
- The Gateway validates this JWT. Downstream microservices do NOT receive the JWT. Instead, they receive the identity via injected headers:
  - `X-User-Id`
  - `X-Username`

## Status Codes
- `200 OK`: Successful GET, PUT, PATCH.
- `201 Created`: Successful POST.
- `401 Unauthorized`: Invalid Token or Missing Token (except for `/api/auth/login/` and `/api/auth/register/`).
- `403 Forbidden`: Authenticated, but lacks permissions.

## Payload Conventions
- `auth-service` saves `preferences` as a flexible, schemaless object allowing partial updates (`PATCH`).
- `finance-service` denormalizes categories into transactions (`category_name`, `category_color`, `category_icon`) so the frontend doesn't have to perform joins.
- Pagination is handled via query params: `?page=1` or `?limit=10`.
