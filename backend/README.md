# Backend Services

Spring Boot microservice backend for Vibe Engineering. Chi tiết luồng và API contract nằm ở `../docs/`.

## Modules

- `eureka-server`: service discovery
- `api-gateway`: API ingress, JWT validation, route forwarding
- `auth-service`: register/login/profile/preferences
- `finance-service`: categories, transactions, statistics, expenses
- `notification-service`: notification listing and read-state APIs

## Runtime map

- Eureka: `http://localhost:8761`
- Gateway: `http://localhost:8080`
- Auth service: `http://localhost:8081`
- Finance service: `http://localhost:8082`
- Notification service: `http://localhost:8083`
- MongoDB: `mongodb://localhost:27017`

## Data

- MongoDB là datastore chính
- Mỗi service có database riêng:
  - `auth_db`
  - `finance_db`
  - `notification_db`
- MongoDB chạy với replica set `rs0`
- `finance-service` seed category mặc định khi DB trống
- `notification-service` seed notification mẫu khi user chưa có dữ liệu

## Chạy backend

```powershell
docker compose up --build
```

## Smoke test

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\smoke-test.ps1
```

## API and auth notes

- Public auth endpoints:
  - `POST /api/auth/login/`
  - `POST /api/auth/register/`
- Các endpoint còn lại yêu cầu:
  - `Authorization: Token <jwt>`
- Gateway strip `/api` trước khi forward
- Gateway inject `X-User-Id` và `X-Username` sau khi verify JWT
- Transaction và notification ID đều là numeric sequence IDs để frontend render ổn định

