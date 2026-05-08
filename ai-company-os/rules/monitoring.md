# Monitoring & Observability Rules

## Purpose
You cannot fix what you cannot see. Comprehensive monitoring ensures we detect downtime, performance bottlenecks, and errors before our users report them.

## The Three Pillars of Observability
1. **Metrics:** Quantitative data (e.g., CPU usage, Request latency, HTTP 500 rate). Tool: Prometheus / Datadog.
2. **Logs:** Discrete event records (e.g., "User 123 failed login"). Tool: ELK Stack, Datadog.
3. **Tracing:** Tracking a single request as it travels across multiple microservices. Tool: OpenTelemetry, Jaeger.

## Logging Standards
- All logs must be output in **JSON format** to allow easy querying and filtering in log aggregators.
- Always include Context:
  - `requestId` (Trace ID from the API Gateway)
  - `userId` (if authenticated)
  - `action` (e.g., `checkout_started`)
- **Scrubbing:** Ensure logging middleware masks credit card numbers, passwords, and tokens.

## Frontend & Exception Monitoring
- Use **Sentry** (or similar) to capture unhandled client-side Javascript exceptions and backend crashes.
- Upload Source Maps to Sentry during the CI build process so stack traces are readable.

## Alerting
- Set up alerts for critical thresholds:
  - High error rate (e.g., > 1% of requests returning 5xx in the last 5 minutes).
  - High latency (e.g., p95 response time > 1000ms).
  - High CPU/Memory usage (> 85%).
- Route critical alerts to PagerDuty/Slack for immediate on-call engineering response.

## Performance Budgets
- Set performance budgets in CI using Lighthouse CI. Fail builds if mobile performance score drops below 80, or if Core Web Vitals (LCP, CLS, FID) degrade.
