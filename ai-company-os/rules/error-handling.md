# Error Handling Rules

## Purpose
Ensure that errors are captured gracefully, logged comprehensively for debugging, and presented safely to the user without leaking system internals.

## Centralized Error Handling
- **Backend:** All unhandled exceptions must bubble up to a centralized Global Exception Filter/Middleware. Never return raw stack traces in HTTP responses.
- **Frontend:** Use Error Boundaries (`react-error-boundary`) to catch rendering errors and display a graceful fallback UI instead of a blank white screen.

## Exception Mapping (Backend)
Define custom application errors that extend the base `Error` class and map them to appropriate HTTP status codes.
- `ValidationError` -> 400 Bad Request
- `UnauthorizedError` -> 401 Unauthorized
- `ForbiddenError` -> 403 Forbidden
- `NotFoundError` -> 404 Not Found
- `ConflictError` -> 409 Conflict
- `InternalServerError` -> 500 Internal Server Error

## Validation
- Validate **all** incoming data at the boundaries using schema validation libraries (e.g., `Zod`, `class-validator`).
- Do not rely on frontend validation alone; the backend must strictly re-validate all inputs.

## Logging Strategy
- Log the error context: User ID, Request ID (Trace ID), Timestamp, and the exact input parameters that caused the error.
- Use proper log levels:
  - `ERROR`: Operations failed, user is blocked. Needs immediate attention.
  - `WARN`: Recoverable errors, retries triggered, or suspicious behavior.
  - `INFO`: Normal application flow (e.g., user logged in).
- Scrub sensitive data (PII, passwords, API tokens) before logging.

## Retry Policy & Fallbacks
- For network calls to external APIs or microservices, implement exponential backoff with jitter retries to handle transient failures.
- Implement Circuit Breakers to prevent cascading failures if a downstream service is completely down.
- Frontend should implement UI fallbacks (e.g., displaying cached data if a fetch fails, with an offline banner).
