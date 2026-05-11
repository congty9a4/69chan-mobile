# Tính năng: Authentication

Đăng nhập, đăng ký, đăng nhập guest, refresh token, đăng xuất.

## Trạng thái

| Sub-feature | Trạng thái | Ghi chú |
|---|---|---|
| Login (email/password) | ✅ Hoạt động | Đã wire UI ↔ ViewModel ↔ Repository ↔ API |
| Register | ✅ Hoạt động | Có validate email + password length |
| Guest login | ✅ Hoạt động | Logic có, UI chưa expose nút (gọi qua `loginAsGuest()`) |
| Refresh token | 🚧 WIP | TokenAuthenticator đang refactor — xem [Known issues](#known-issues) |
| Logout | ⚠️ Cần verify | Phụ thuộc Fix A (`@MainClient` cho `AuthAPI`) |
| Forgot password | ❌ Chưa làm | Route placeholder trong `AppNav.kt` |
| Google/Facebook login | ❌ Chưa làm | UI button có, handler `/* TODO */` |

## API endpoints

Định nghĩa trong [AuthAPI.kt](../../app/src/main/java/com/mycom/chan/data/remote/api/AuthAPI.kt):

| Method | Endpoint | Auth | Mô tả |
|---|---|---|---|
| POST | `/auth/login` | No (`@Headers("No-Auth: true")`) | Đăng nhập với email + password |
| POST | `/auth/register` | No | Đăng ký với username + email + password |
| POST | `/auth/guest` | No | Tạo session khách |
| POST | `/auth/refresh-token` | No (refresh token nằm trong body) | Lấy access token mới |
| POST | `/auth/logout` | **Yes** (Bearer required) | Hủy session phía server |

`/auth/refresh-token` có 2 phiên bản trong interface:
- `suspend fun refreshToken(...)` — cho Repository gọi từ ViewModel.
- `fun refreshTokenSync(...): Call<...>` — cho `TokenAuthenticator` (sync context).

## Flow: Login

```
[User nhập email + password]
        │
        ▼
LoginViewModel.login()
        │
        ├─ Validate (email regex, password length ≥ 6)
        │      └─ Fail → emit LoginEvent.ShowError → snackbar
        │
        ├─ authRepository.login(email, password)
        │      │
        │      ├─ authAPI.login(LoginRequest)         [qua @MainClient, No-Auth header]
        │      │      └─ HTTP 200 + body.success = true
        │      │            ├─ tokenManager.saveAccessToken(token)
        │      │            ├─ tokenManager.saveRefreshToken(refresh)
        │      │            └─ Return Result.Success(Authentication)
        │      │      └─ HTTP error → Result.Error(message)
        │      │
        │      └─ catch exception → Result.Error
        │
        ├─ Result.Success → emit LoginEvent.loginSuccess
        │      └─ UI snackbar "Login Successfully"
        │      └─ Navigate đến Feed (popUpTo Login inclusive)
        │
        └─ Result.Error → emit LoginEvent.ShowError
               └─ Snackbar lỗi
```

State + Event:
```kotlin
data class LoginState(email, password, rememberMe, isLoading)
sealed interface LoginEvent {
    object loginSuccess
    data class ShowError(message: String)
}
```

## Flow: Register

Tương tự Login, thêm:
- Validate `confirmPassword == password`.
- Sau success → navigate về Login (chưa auto-login).

> **TODO:** Sau register có thể auto-login luôn (tận dụng token đã trả về). Hiện đang require user nhập lại.

## Flow: Refresh token (transparent — user không thấy)

Diễn ra tự động khi request bị 401:

```
[Request bất kỳ → 401]
        │
        ▼
OkHttp gọi TokenAuthenticator.authenticate()    [sync, dispatcher thread]
        │
        ├─ tokenRefresher.refreshTokenBlocking(failedToken)
        │      │
        │      ├─ synchronized(refreshLock) {
        │      │      Double-check: token đã đổi? → return true (skip API)
        │      │      Đọc refresh token (sync, từ cache)
        │      │      authApi.refreshTokenSync().execute()    [qua @AuthClient trần]
        │      │      Save token mới (sync) → _accessTokenFlow emit
        │      │      Return true
        │      ├─ }
        │      │
        │      └─ Refresh fail vĩnh viễn (401/403) → clearTokenBlocking → return false
        │      └─ Network error → return false (KHÔNG clear token)
        │
        ├─ Refresh OK → tạo Request mới với Bearer mới → OkHttp retry
        └─ Refresh fail → return null → request fail lên ViewModel
```

Chi tiết kỹ thuật trong [token-management.md](../architecture/token-management.md#token-refresh-flow).

## Flow: Logout

```
User bấm Logout (Menu)
        │
        ▼
AuthViewModel.logout()
        │
        ├─ authRepository.logout()
        │      │
        │      ├─ authAPI.logout(LogoutRequest(refreshToken, accessToken))
        │      │      └─ Cần qua @MainClient để có Bearer header
        │      │      └─ HTTP 200 → Result.Success(true)
        │      │
        │      └─ Lưu ý: hiện chưa có code clear token sau logout success
        │           → cần thêm `tokenManager.clearToken()` trong logout()
        │
        ├─ Result.Success → uiState.update(isLoggedIn = false, currentUser = null)
        │      └─ AuthViewModel collect _accessTokenFlow → cần clearToken để emit null
        │
        └─ Result.Error → snackbar lỗi
```

> **🚧 Bug hiện tại:** [ImpleAuthRepository.logout()](../../app/src/main/java/com/mycom/chan/data/repository/ImpleAuthRepository.kt) chưa gọi `tokenManager.clearToken()` sau khi server trả OK. Cần thêm để token thực sự bị xoá khỏi `_accessTokenFlow`.

## Component liên quan

### Domain

- [AuthRepository.kt](../../app/src/main/java/com/mycom/chan/domain/repository/AuthRepository.kt) — interface.
- [Authentication.kt](../../app/src/main/java/com/mycom/chan/domain/model/Authentication.kt) — domain model `(accessToken, refreshToken, user)`.
- [User.kt](../../app/src/main/java/com/mycom/chan/domain/model/User.kt) — domain user.

### Data

- [ImpleAuthRepository.kt](../../app/src/main/java/com/mycom/chan/data/repository/ImpleAuthRepository.kt) — implementation.
- [AuthAPI.kt](../../app/src/main/java/com/mycom/chan/data/remote/api/AuthAPI.kt) — Retrofit interface.
- DTO: [LoginRequest](../../app/src/main/java/com/mycom/chan/data/remote/dto/request/LoginRequest.kt), [RegisterRequest](../../app/src/main/java/com/mycom/chan/data/remote/dto/request/RegisterRequest.kt), [RefreshRequest](../../app/src/main/java/com/mycom/chan/data/remote/dto/request/RefreshRequest.kt), [LogoutRequest](../../app/src/main/java/com/mycom/chan/data/remote/dto/request/LogoutRequest.kt), [AuthResponseDTO](../../app/src/main/java/com/mycom/chan/data/remote/dto/response/AuthResponse.kt).
- [AuthenticationMapper.kt](../../app/src/main/java/com/mycom/chan/data/mapper/AuthenticationMapper.kt) — `AuthResponseDTO → Authentication`.

### Presentation

- ViewModel:
  - [LoginViewModel.kt](../../app/src/main/java/com/mycom/chan/presentation/viewmodel/LoginViewModel.kt)
  - [RegisterViewModel.kt](../../app/src/main/java/com/mycom/chan/presentation/viewmodel/RegisterViewModel.kt)
  - [AuthViewModel.kt](../../app/src/main/java/com/mycom/chan/presentation/viewmodel/AuthViewModel.kt) — observe global auth state.
- Page:
  - [Login.kt](../../app/src/main/java/com/mycom/chan/presentation/page/Login.kt)
  - [Register.kt](../../app/src/main/java/com/mycom/chan/presentation/page/Register.kt)
- Component:
  - [LoginInput.kt](../../app/src/main/java/com/mycom/chan/presentation/components/login_register/LoginInput.kt), [LoginButton.kt](../../app/src/main/java/com/mycom/chan/presentation/components/login_register/LoginButton.kt)
  - [RegisterInput.kt](../../app/src/main/java/com/mycom/chan/presentation/components/login_register/RegisterInput.kt), [RegisterButton.kt](../../app/src/main/java/com/mycom/chan/presentation/components/login_register/RegisterButton.kt)
  - [Menu.kt](../../app/src/main/java/com/mycom/chan/presentation/components/Menu.kt) — drawer hiển thị user info + logout.

### Validation

Hiện đang regex inline trong cả `LoginViewModel` và `RegisterViewModel`:

```kotlin
val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
```

> **TODO:** Tách ra util chung (ví dụ `domain/validation/EmailValidator.kt`) để DRY và dễ test.

## Known issues

1. **🚧 `TokenAuthenticatorImple` đang gọi `refreshTokenBlocking()` chưa tồn tại** — project hiện không build. Đang implement.
2. **🐛 `ImpleAuthRepository.logout()` chưa gọi `tokenManager.clearToken()`** — sau logout, `_accessTokenFlow` vẫn giữ token cũ → menu vẫn hiển thị user.
3. **🐛 `AuthRepository.logout()` interface có default `Result.Success(true)`** — gây nhầm lẫn, nên đổi thành abstract method.
4. **🐛 `LogoutRequest` field naming snake/camel không nhất quán** với `RefreshRequest` — cần check với backend.
5. **⚠️ Email validation regex duplicate** giữa Login và Register VM.

## Test plan (khi viết test sau này)

| Tình huống | Kỳ vọng |
|---|---|
| Login email rỗng | Snackbar "Email is required" |
| Login email sai format | Snackbar "Email is invalid" |
| Login password < 6 ký tự | Snackbar "Password must be at least 6 characters" |
| Login đúng credential | Save token, navigate Feed, snackbar success |
| Login sai credential | Snackbar lỗi từ server |
| Register confirmPassword ≠ password | Snackbar "Password is not match" |
| App restart sau login | Token còn, AuthViewModel emit isLoggedIn=true |
| Token expire khi đang dùng app | Authenticator refresh trong suốt, user không thấy gì |
| Refresh token cũng expire | Force logout, navigate về Login |
| Logout | Token clear, menu chuyển "Vui lòng đăng nhập" |
