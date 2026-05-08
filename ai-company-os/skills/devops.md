# Skill: DevOps & CI/CD

## Concepts
DevOps is the union of people, process, and technology to continually provide value to customers. CI/CD (Continuous Integration / Continuous Deployment) automates the building, testing, and deployment of code.

## Best Practices
- **Infrastructure as Code (IaC):** Manage servers, databases, and networks using code (Terraform, Pulumi, or AWS CDK). Never click through cloud consoles to provision resources manually.
- **Immutable Infrastructure:** Do not SSH into a server to update code or fix things. Build a new Docker container, deploy it, and destroy the old one.
- **Shift-Left Security:** Run security scans (SAST, Dependency audits, Secret scanning) in the CI pipeline *before* code is merged.

## Workflow
1. **Code Commit:** Developer pushes code to a feature branch.
2. **CI Pipeline (GitHub Actions):**
   - Linting & Prettier check.
   - Unit & E2E Tests run.
   - Docker image is built.
3. **Merge & CD Pipeline:**
   - Code merged to `main`.
   - Docker image tagged with Git SHA and pushed to Registry.
   - Kubernetes cluster applies the new image tag.
4. **Post-Deploy:** Smoke tests verify production is healthy.

## Examples
*A standard GitHub Actions CI snippet:*
```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with: { node-version: '20' }
      - run: npm ci
      - run: npm run lint
      - run: npm run test:coverage
```

## AI Usage Strategy
- Use AI to generate complex `docker-compose.yml` or Kubernetes manifest files (`deployment.yaml`, `service.yaml`).
- Ask AI to translate a manual AWS architecture into Terraform code.
- Paste a failing GitHub Actions log into Claude to identify why the CI pipeline broke.

## Common Mistakes
- **Deploying on Fridays:** Unless you have extreme confidence in your rollback mechanisms and automated tests.
- **Snowflake Servers:** Servers that have been manually configured over years and nobody knows how to recreate them if they crash.

## Optimization Techniques
- **Docker Layer Caching:** Order your `Dockerfile` to copy `package.json` and run `npm install` BEFORE copying source code. This caches the dependencies step unless package.json changes.
- **Blue/Green Deployments:** Deploying the new version alongside the old version, testing it, and then switching the load balancer traffic instantly. Zero downtime.
