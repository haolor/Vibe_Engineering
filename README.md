# Vibe Engineering

Nền tảng quản lý tài chính cá nhân theo kiến trúc microservice. Repository này chứa một React SPA ở frontend và một cụm Spring Boot service ở backend, giao tiếp qua API Gateway và service discovery Eureka.

Tài liệu này là điểm vào nhanh cho người mới và cho AI sinh code. Nếu cần hiểu sâu luồng đi, đọc tiếp các file trong `docs/`.

## Mục tiêu sản phẩm

Ứng dụng cho phép người dùng:

- Đăng ký, đăng nhập và khôi phục phiên
- Quản lý giao dịch thu/chi theo danh mục
- Xem dashboard tài chính tổng quan
- Xem thống kê theo ngày, theo danh mục và theo khoảng thời gian
- Tuỳ biến giao diện và cấu hình thông báo
- Nhận danh sách thông báo, đánh dấu từng cái hoặc tất cả là đã đọc

## Kiến trúc tổng thể

### Service map

- `eureka-server` trên port `8761`: service discovery
- `api-gateway` trên port `8080`: entrypoint cho frontend, JWT filter, route forwarding
- `auth-service` trên port `8081`: register, login, profile, preferences
- `finance-service` trên port `8082`: categories, transactions, statistics, expenses
- `notification-service` trên port `8083`: notifications và trạng thái đọc
- `mongo` trên port `27017`: datastore, replica set `rs0`

### Luồng request chuẩn

1. Frontend gọi API qua `baseURL: /api`
2. Gateway chặn request và kiểm tra JWT ở header `Authorization: Token <jwt>`
3. Gateway bỏ prefix `/api` trước khi chuyển tiếp
4. Gateway gắn thêm `X-User-Id` và `X-Username` vào request nội bộ
5. Service đích xử lý nghiệp vụ và đọc/ghi MongoDB
6. Response trả lại frontend theo đúng shape mà UI đang mong đợi

### Quy ước xác thực

- Header auth dùng `Token`, không dùng `Bearer`
- Endpoint công khai chỉ có `POST /api/auth/login/` và `POST /api/auth/register/`
- Các endpoint còn lại cần `X-User-Id` do gateway sinh ra sau khi verify JWT

## Thành phần frontend

- `src/App.jsx`: router, private routes, provider tree
- `src/contexts/AuthContext.jsx`: login, register, logout, khôi phục phiên
- `src/contexts/PreferencesContext.jsx`: tải và cập nhật preferences, áp theme ngay sau khi đổi
- `src/components/Layout.jsx`: sidebar, notification bell, polling 30 giây
- `src/pages/Dashboard.jsx`: summary cards và recent expenses
- `src/pages/Transactions.jsx`: CRUD giao dịch + pagination + mobile/desktop view
- `src/pages/Statistics.jsx`: biểu đồ theo khoảng ngày và theo danh mục
- `src/pages/Settings.jsx`: theme, primary color, notification settings, dashboard chart type
- `src/services/api.js`: axios client, tự gắn token và xử lý `FormData`

### State và UX quan trọng

- Token được lưu trong `localStorage`
- Frontend tự gọi `/api/auth/profile/` để phục hồi user khi reload
- Preferences có theme sáng/tối/tự động và màu chủ đạo qua biến CSS `--primary-color`
- Layout tải notifications và unread count mỗi 30 giây
- Dashboard/Statistics dùng dữ liệu từ finance-service, không tính toán ở client

## Thành phần backend

### `auth-service`

- Đăng ký tạo user mới, tự gán preferences mặc định
- Đăng nhập kiểm tra username/password theo logic hiện tại của codebase
- JWT được issue ở auth-service và verify ở gateway
- Preferences được lưu trực tiếp trong document user

### `finance-service`

- Seed danh mục mặc định nếu database trống
- Transaction ID là số sequence tăng dần
- Danh sách giao dịch có phân trang server-side với page size 20
- Statistics aggregate theo ngày và theo danh mục

### `notification-service`

- Seed notifications mẫu nếu user chưa có dữ liệu
- Notification ID là số sequence tăng dần
- API trả `results` cho list, `unread_count` cho số chưa đọc

## Data model và quy ước payload

- MongoDB mỗi service một database riêng:
  - `auth_db`
  - `finance_db`
  - `notification_db`
- `finance-service` lưu transaction kèm `category_name`, `category_icon`, `category_color`, `category_type` để frontend render nhanh
- `notification-service` lưu `type`, `title`, `message`, `created_at`, `is_read`
- `auth-service` lưu `preferences` dưới dạng object linh hoạt để patch từng phần
- Các trang frontend đang phụ thuộc vào trailing slash ở endpoint, ví dụ `/auth/login/`, `/transactions/`, `/notifications/`

## API contract ngắn gọn

### Auth

- `POST /api/auth/register/`
- `POST /api/auth/login/`
- `GET /api/auth/profile/`
- `GET /api/auth/preferences/`
- `PATCH /api/auth/preferences/`

### Finance

- `GET /api/categories/`
- `GET /api/transactions/?page={n}`
- `POST /api/transactions/`
- `PUT /api/transactions/{id}/`
- `DELETE /api/transactions/{id}/`
- `GET /api/transactions/statistics/?period=all`
- `GET /api/transactions/statistics/?start_date=YYYY-MM-DD&end_date=YYYY-MM-DD`
- `GET /api/transactions/expenses/`

### Notifications

- `GET /api/notifications/?limit=10`
- `GET /api/notifications/unread_count/`
- `POST /api/notifications/{id}/mark_read/`
- `POST /api/notifications/mark_all_read/`

## Cấu trúc repository

```text
.
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── contexts/
│   │   ├── pages/
│   │   └── services/
│   ├── index.html
│   └── vite.config.js
├── backend/
│   ├── eureka-server/
│   ├── api-gateway/
│   ├── auth-service/
│   ├── finance-service/
│   ├── notification-service/
│   ├── scripts/
│   ├── docker-compose.yml
│   └── pom.xml
├── docs/
│   ├── PROJECT_CONTEXT.md
│   ├── API_CONTRACT.md
│   └── AI_CODING_GUIDE.md
├── static/frontend/
└── README.md
```

## Yêu cầu chạy local

- Node.js 18+
- npm 9+
- Docker Desktop

Tuỳ chọn nếu không dùng Docker cho backend:

- JDK 17
- Maven 3.9+

## Chạy local

### Backend

```powershell
cd .\backend
docker compose up --build
```

Sau khi chạy:

- Gateway: `http://localhost:8080`
- Eureka UI: `http://localhost:8761`
- MongoDB: `mongodb://localhost:27017`

### Frontend

Backend hiện đã cấu hình gateway ở `http://localhost:8080`. Nếu `frontend/vite.config.js` trỏ sang port khác, hãy đổi về `8080` trước khi chạy.

```powershell
cd .\frontend
npm install
npm run dev
```

Frontend dev server thường chạy ở `http://localhost:3000`.

### Smoke test

```powershell
cd .\backend
powershell -ExecutionPolicy Bypass -File .\scripts\smoke-test.ps1
```

Script kiểm tra các luồng chính:

- register
- profile/preferences
- create transaction
- statistics/expenses/categories
- notifications và mark read

## Build production frontend

```powershell
cd .\frontend
npm run build
npm run preview
```

Build output được phục vụ từ `static/frontend`.

## Tài liệu chi tiết (AI-OS)

Hệ thống AI-first Operating System đã được cấu hình trong thư mục [ai-company-os/README.md](ai-company-os/README.md). Dưới đây là các tài liệu then chốt để tham khảo:

### Tham khảo nhanh
- **Agents:** [Frontend](ai-company-os/agents/frontend.md) | [Backend](ai-company-os/agents/backend.md) | [Architect](ai-company-os/agents/system-architect.md) | [PM](ai-company-os/agents/project-manager.md) | [QA](ai-company-os/agents/qa.md) | [UI/UX](ai-company-os/agents/ui-ux-designer.md) | [SEO](ai-company-os/agents/copywriter-seo.md)
- **Commands:** [Deploy](ai-company-os/commands/deploy.md) | [Fix Issue](ai-company-os/commands/fix-issue.md) | [Review](ai-company-os/commands/review.md)
- **Rules:** [Project Structure](ai-company-os/rules/project-structure.md) | [API Conventions](ai-company-os/rules/api-conventions.md) | [Tech Stack](ai-company-os/rules/tech-stack.md) | [System Design](ai-company-os/rules/system-design.md) | [Clean Code](ai-company-os/rules/clean-code.md) | [Security](ai-company-os/rules/security.md) | [Testing](ai-company-os/rules/testing.md)
- **Skills:** [AI Workflow](ai-company-os/skills/ai-workflow.md) | [Database Design](ai-company-os/skills/database-design.md) | [Prompt Engineering](ai-company-os/skills/prompt-engineering.md)


- `auth-service`
  - `MONGODB_URI` (mặc định: `mongodb://localhost:27017/auth_db`)
- `finance-service`
  - `MONGODB_URI` (mặc định: `mongodb://localhost:27017/finance_db`)
- `notification-service`
  - `MONGODB_URI` (mặc định: `mongodb://localhost:27017/notification_db`)

### JWT

- Gateway và auth-service phải dùng cùng secret để verify token đúng.
- Hiện secret đang để trong config dạng demo; nên chuyển sang env secret manager khi deploy thật.

## Hành Vi Bảo Mật Hiện Tại

- Header auth chuẩn: `Authorization: Token <jwt>`.
- Gateway cho phép anonymous ở:
  - `/api/auth/login/`
  - `/api/auth/register/`
- Các route còn lại yêu cầu token hợp lệ.
- Gateway inject identity xuống service qua:
  - `X-User-Id`
  - `X-Username`

## Known Limitations (Hiện Trạng)

- Frontend proxy mặc định vẫn trỏ `8000`, chưa khớp gateway `8080` nếu chưa đổi thủ công.
- Password đang lưu/so sánh dạng plain text trong auth-service (mức demo, chưa production-grade).
- JWT secret hardcoded trong config mặc định.
- Cài đặt default frontend và backend có một vài giá trị khởi tạo khác nhau (ví dụ theme/threshold) trước khi sync từ server.

## Gợi Ý Nâng Cấp Tiếp Theo

- Hash password bằng `BCryptPasswordEncoder`.
- Externalize JWT secret qua env/secret manager.
- Thêm refresh token flow.
- Thêm migration/index strategy cho MongoDB.
- Thêm test integration tự động trong CI.
- Đồng bộ frontend proxy mặc định về `8080` để giảm thao tác setup.

## Dừng Hệ Thống

- Dừng frontend: `Ctrl + C`
- Dừng backend:

```powershell
cd .\backend
docker compose down
```
