# Chan — Tài liệu kỹ thuật

Tài liệu nội bộ mô tả kiến trúc, kỹ thuật và tính năng của ứng dụng Chan (Android social media app).

## Tech stack

| Layer | Công nghệ |
|---|---|
| Ngôn ngữ | Kotlin 2.1.0 |
| UI | Jetpack Compose, Material3, Navigation Compose |
| DI | Hilt 2.51.1 |
| Network | Retrofit 2.11 + OkHttp + Gson |
| Async | Coroutines + Flow/StateFlow |
| Local storage | Android `AccountManager` (token), in-memory cache |
| Image loading | Coil |
| Monitoring | Sentry |
| Min SDK / Target | 24 / 35 |

## Cấu trúc tài liệu

### `architecture/`
Mô tả các kỹ thuật xuyên suốt project — áp dụng cho mọi feature.

- [overview.md](architecture/overview.md) — Tổng quan Clean Architecture, phân chia layer, quy ước import.
- [networking.md](architecture/networking.md) — Retrofit, OkHttp, Interceptor, Authenticator, dual-client pattern.
- [token-management.md](architecture/token-management.md) — Lưu trữ token, reactive state, dual-API sync/suspend.

### `features/`
Mô tả từng tính năng cụ thể — flow, component, trạng thái phát triển.

- [auth.md](features/auth.md) — Đăng nhập, đăng ký, guest, refresh token, đăng xuất.

## Quy ước mở rộng tài liệu

Khi thêm tính năng mới hoặc kỹ thuật mới, tạo file `.md` theo template sau:

**File feature** (`features/<tên>.md`):
1. Tóm tắt tính năng (1-2 câu)
2. API endpoints (nếu có)
3. Flow diagram chính
4. Component liên quan (file paths)
5. Trạng thái: ✅ done / 🚧 WIP / ⚠️ broken
6. Known issues hoặc TODO

**File architecture** (`architecture/<tên>.md`):
1. Vấn đề được giải quyết
2. Giải pháp / pattern dùng
3. File liên quan
4. Trade-off / lưu ý
5. Cách áp dụng cho feature mới

## Trạng thái tổng quan các tính năng

| Feature | Trạng thái | Ghi chú |
|---|---|---|
| Auth — Login/Register | ✅ Hoạt động | Đã wire end-to-end |
| Auth — Refresh token | 🚧 WIP | TokenAuthenticator đang refactor (bỏ runBlocking) |
| Auth — Logout | ⚠️ Cần verify | Phụ thuộc client setup `@MainClient` |
| Feed/Posts | 🚧 WIP | UI có Scaffold + drawer, chưa wire ViewModel + API |
| Chat | 🚧 WIP | UI có, ViewModel đang comment, Repository chưa bind DI |
| Forgot Password | ❌ Chưa làm | Placeholder route trong AppNav |
| Google/Facebook login | ❌ Chưa làm | Có UI button, chưa có logic |

Cập nhật bảng này mỗi khi đổi trạng thái feature.
