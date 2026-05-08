# Skill: Scalability

## Concepts
Scalability is the capability of a system to handle a growing amount of work by adding resources.
- **Vertical Scaling (Scaling Up):** Adding more CPU/RAM to a single server. (Easy, but has a hard limit and causes downtime).
- **Horizontal Scaling (Scaling Out):** Adding more servers to a pool. (Harder to build, but infinite capacity and highly available).

## Best Practices
- **Statelessness:** Web servers must not hold any session data in memory. Store sessions in Redis or use JWTs. This allows any server to handle any request.
- **Asynchronous Processing:** Move slow tasks (email sending, image processing) off the main HTTP thread into background workers (e.g., BullMQ, AWS SQS).
- **Auto-Scaling:** Configure infrastructure (Kubernetes HPA, AWS ASG) to automatically add instances when CPU utilization crosses 75%.

## Workflow
1. **Load Testing:** Use tools like K6 or Artillery to simulate 10,000 concurrent users.
2. **Identify the Choke Point:** Did the DB run out of connections? Did the Node process hit 100% CPU?
3. **Architectural Shift:** Implement connection pooling (PgBouncer), caching layers, or read replicas.

## Examples
*Scenario: App Flash Traffic.*
- **Problem:** Database crashes under the load of 50,000 users reading the statistics.
- **Scalable Solution:** Serve the cached statistics. Run multiple instances of `finance-service` and let `eureka-server` load balance the requests across them.

## AI Usage Strategy
- Ask System Architect AI: "We expect a 100x traffic spike next week. Review my architecture diagram and identify the single points of failure."
- Ask AI to generate Terraform scripts to provision auto-scaling groups and Redis clusters.

## Common Mistakes
- **Distributed Monolith:** Splitting an app into microservices that all synchronously call each other over HTTP. If one slows down, they all crash.
- Not using Connection Pooling for Serverless functions (exhausting database connections instantly).

## Optimization Techniques
- **Sharding / Partitioning:** Splitting a massive database table across multiple physical servers.
- **Edge Computing:** Moving compute logic closer to the user using Cloudflare Workers or Vercel Edge Functions.
