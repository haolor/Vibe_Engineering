# Security Rules

## Purpose
Security is a day-zero requirement. We strictly adhere to OWASP Top 10 principles to protect user data and enterprise infrastructure from malicious actors.

## Authentication & Authorization
- **Passwords:** Never store plain text. Hash passwords using `bcrypt` (cost >= 12) or `Argon2`.
- **JWT (JSON Web Tokens):** 
  - Keep expiration times short (e.g., 15 minutes).
  - Use secure HTTP-only, `SameSite=Strict` cookies to store refresh tokens to prevent XSS theft.
  - Do not put sensitive data (PII) in the JWT payload (it is base64 encoded, not encrypted).
- **RBAC:** Implement Role-Based Access Control. Every API endpoint must explicitly check the user's role/permissions.

## Prevention of Common Vulnerabilities (OWASP)
1. **SQL Injection:** Always use an ORM (Prisma/Drizzle) or parameterized queries. NEVER concatenate strings to form SQL.
2. **XSS (Cross-Site Scripting):** Rely on React's automatic escaping. If `dangerouslySetInnerHTML` must be used, sanitize the input with `DOMPurify` first.
3. **CSRF (Cross-Site Request Forgery):** Use Anti-CSRF tokens if using cookie-based session auth, or strictly configure `SameSite` cookie attributes.
4. **Rate Limiting & Brute Force:** Apply strict rate limiting on `/login`, `/register`, and `/forgot-password` endpoints (e.g., max 5 attempts per 15 minutes).

## Secrets Management
- NEVER commit `.env` files or hardcode API keys.
- Use a Secret Manager (AWS Secrets Manager, HashiCorp Vault, Vercel Env Vars) in production.
- Use tools like `git-secrets` or GitHub Advanced Security to scan for accidental secret commits.

## Data Protection
- **Encryption in Transit:** All communication must occur over TLS 1.2+ (HTTPS). HTTP traffic must redirect to HTTPS.
- **Encryption at Rest:** Ensure databases (RDS, S3) are configured to encrypt data at rest using KMS keys.
