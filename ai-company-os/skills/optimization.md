# Skill: Optimization

## Concepts
Optimization is the process of modifying a system to make it work more efficiently or use fewer resources. It encompasses Frontend (rendering, bundle size), Backend (CPU, memory), and Network optimization.

## Best Practices
- **Measure First:** Never optimize blindly. Use Lighthouse, Chrome DevTools Profiler, or Datadog APM to find the actual bottleneck.
- **The 80/20 Rule:** 80% of performance issues come from 20% of the code (usually DB queries or massive React re-renders).
- **Trade-offs:** Optimization often trades memory for CPU (e.g., caching), or code readability for speed.

## Workflow
1. **Profile:** Run a load test or performance audit.
2. **Identify Bottleneck:** E.g., "The 'Get Dashboard' API takes 2.5 seconds."
3. **Formulate Hypothesis:** "The DB query lacks an index on the `status` column."
4. **Test & Measure:** Add the index, rerun the test. If it drops to 50ms, the hypothesis was correct.

## Examples
- **Frontend:** Code-splitting routes using `next/dynamic` so users only download the JS they need for the current page.
- **Backend:** Replacing a nested loop `O(n^2)` with a Hash Map lookup `O(n)`.
- **Network:** Compressing payloads using Brotli/Gzip and enabling HTTP/2 multiplexing.

## AI Usage Strategy
- Paste a slow SQL query into GPT-4o and ask: "Optimize this PostgreSQL query and provide the required `CREATE INDEX` statements."
- Paste a React component into Claude and ask: "Why is this component re-rendering infinitely, and how can I fix it using `useMemo` or `useCallback`?"

## Common Mistakes
- **Premature Optimization:** Wasting days optimizing a function that runs once a month and takes 10ms.
- Ignoring Network Latency: Optimizing the DB to save 5ms while the API payload is 5MB and takes 3 seconds to download.

## Optimization Techniques
- **Memoization:** Caching the result of expensive function calls.
- **Debouncing/Throttling:** Limiting the rate at which a function (like search input) fires.
- **Tree-shaking:** Ensuring unused exports are stripped from the final Javascript bundle.
