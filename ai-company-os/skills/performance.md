# Skill: Application Performance

## Concepts
Performance engineering ensures the application feels instantaneous to the user. It is measured in metrics like Core Web Vitals (Frontend) and Latency/Throughput (Backend).

## Best Practices
- **Frontend Core Web Vitals:**
  - **LCP (Largest Contentful Paint):** Target < 2.5s. Optimize hero images, use SSR/SSG.
  - **FID (First Input Delay) / INP:** Target < 100ms. Reduce main-thread blocking JavaScript.
  - **CLS (Cumulative Layout Shift):** Target < 0.1. Set explicit width/height on images and ads to prevent layout jumping.
- **Backend Latency:**
  - Target API response times < 200ms.
  - Offload long-running tasks to queues.

## Workflow
1. **Audit:** Run a Lighthouse report in Chrome Incognito. Check the "Performance" tab.
2. **Diagnose:** Use the Chrome DevTools "Network" tab to find slow requests, or the "Performance" timeline to find long JS execution tasks.
3. **Implement:** Apply fixes (e.g., dynamic imports, image compression).
4. **Verify:** Rerun Lighthouse and compare scores.

## Examples
- **Image Optimization:** Compress images to WebP format manually or via a CDN service, instead of serving 3MB PNGs.
- **Font Loading:** Use `font-display: swap` so text is visible immediately while the custom web font downloads in the background.

## AI Usage Strategy
- Provide a React component to an AI and say: "This component has a low Lighthouse score. Refactor it to improve LCP and reduce CLS. Ensure any heavy third-party libraries are lazy-loaded."
- Ask AI to generate optimal `Cache-Control` headers for different types of static assets.

## Common Mistakes
- **Huge Bundle Sizes:** Importing the entirety of `lodash` or `moment.js` instead of using native JS functions or tree-shakeable libraries (like `date-fns`).
- **Missing Pagination:** Fetching 10,000 records from the backend and rendering them all in the DOM at once (crashes the browser).

## Optimization Techniques
- **Virtualization (Windowing):** Use libraries like `@tanstack/react-virtual` to only render the 10-20 DOM elements currently visible on the screen, even if the list has 100,000 items.
- **Pre-fetching:** Fetching data or JS chunks before the user even clicks the link (using React Router features or custom prefetch logic on hover).
