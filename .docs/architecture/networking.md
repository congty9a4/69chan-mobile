# Networking

Mô tả setup HTTP client, cách auth được xử lý tự động, và pattern dual-client để tránh deadlock.

## Stack

- **Retrofit 2.11** — định nghĩa API qua interface, sinh code gọi.
- **OkHttp** — HTTP client thực tế, hỗ trợ Interceptor + Authenticator.
- **Gson** — serialize/deserialize JSON.

## Sơ đồ tổng quan

```
ViewModel
    │
    ▼
Repository  ──┐
              │ gọi suspend method
              ▼
         AuthAPI / PostAPI / ChatAPI   (Retrofit interface)
              │
              ▼
         Retrofit
              │
              ▼
     ┌────────────────────────────────┐
     │    OkHttpClient (@MainClient)   │   ← cho hầu hết request
     │  ├── AuthenticateInterceptor    │
     │  └── TokenAuthenticator         │
     └─────────────┬──────────────────┘
                   ▼
                Network

     ┌────────────────────────────────┐
     │    OkHttpClient (@AuthClient)   │   ← chỉ cho refresh-token
     │       (không có interceptor)    │
     └─────────────┬──────────────────┘
                   ▼
                Network
```

## Dual-client pattern

[AppModule.kt](../../app/src/main/java/com/mycom/chan/di/AppModule.kt) cung cấp **2 `OkHttpClient`** với qualifier riêng:

| Qualifier | Cấu hình | Dùng cho |
|---|---|---|
| `@MainClient` | Có `AuthenticateInterceptor` + `TokenAuthenticator` | Mọi request có authentication |
| `@AuthClient` | Trần (không có interceptor/authenticator) | Refresh token endpoint |

### Tại sao cần 2 client?

Tình huống: refresh token cũng có thể trả 401 (refresh token hết hạn). Nếu refresh request đi qua `@MainClient`:

```
[refresh request] → 401 → Authenticator trigger → refresh → 401 → ... vòng lặp
```

OkHttp có cơ chế đếm retry (max 3) chặn vòng lặp, nhưng vẫn lãng phí và fragile. Dùng `@AuthClient` (trần) cho refresh:

```
[refresh request qua @AuthClient] → 401 → fail thẳng → handler clear token, force logout
```

Sạch hơn, không recursive.

## `AuthenticateInterceptor`

[AuthenticateInterceptor.kt](../../app/src/main/java/com/mycom/chan/data/remote/interceptor/AuthenticateInterceptor.kt) chạy **trước mỗi request** đi qua `@MainClient`:

1. Thêm header `Accept: application/json` và `Content-Type: application/json`.
2. Nếu request có header `No-Auth: true` → gỡ header đó, không thêm Authorization.
3. Ngược lại → đọc access token từ `TokenManager` (sync) → thêm `Authorization: Bearer <token>`.

Endpoint nào không cần auth (ví dụ login/register/guest) đánh dấu bằng `@Headers("No-Auth: true")` trong Retrofit interface.

```kotlin
@Headers("No-Auth: true")
@POST("auth/login")
suspend fun login(@Body request: LoginRequest): Response<...>
```

## `TokenAuthenticator`

[TokenAuthenticatorImple.kt](../../app/src/main/java/com/mycom/chan/core/network/auth/TokenAuthenticatorImple.kt) chạy **sau khi server trả 401** trên request đã có Authorization:

1. Đếm số retry, nếu > 3 → return null (giả từ chối, fail lên caller).
2. Lấy `failedToken` từ header request gốc.
3. Gọi `tokenRefresher.refreshTokenBlocking(failedToken)`.
4. Nếu refresh thành công → trả về Request mới với token mới → OkHttp tự retry.
5. Nếu refresh fail → return null → request fail.

> **Trạng thái 🚧:** Method `refreshTokenBlocking()` đang trong quá trình bổ sung. Xem [token-management.md](token-management.md) cho chi tiết.

## API hiện có

| API | File | Client | Endpoint |
|---|---|---|---|
| `AuthAPI` | [AuthAPI.kt](../../app/src/main/java/com/mycom/chan/data/remote/api/AuthAPI.kt) | `@MainClient` (sau khi Fix A xong) | `/auth/login`, `/auth/register`, `/auth/guest`, `/auth/refresh-token`, `/auth/logout` |
| `AuthAPI` (refresh-only) | Cùng interface, riêng instance | `@AuthClient` (TokenRefresher) | `/auth/refresh-token` qua `Call<T>` |
| `PostAPI` | [PostAPI.kt](../../app/src/main/java/com/mycom/chan/data/remote/api/PostAPI.kt) | `@MainClient` | `/posts/feed` |
| `ChatAPI` | [ChatAPI.kt](../../app/src/main/java/com/mycom/chan/data/remote/api/ChatAPI.kt) | `@MainClient` | `/conversations/{id}/messages`, `/messages/send` |

## Base URL

Hardcoded trong [AppModule.kt](../../app/src/main/java/com/mycom/chan/di/AppModule.kt):
```
https://six9chan-2.onrender.com/api/
```

> **TODO:** Move to `BuildConfig` cho phép tách dev/staging/prod.

## Timeout

15s cho connect/read/write. Cấu hình trong `provideMainOkHttpClient` và `provideAuthOkHttpClient`.

## Error handling

Repository dùng [BaseRepository.handleResponse()](../../app/src/main/java/com/mycom/chan/data/repository/BaseRepository.kt):

- HTTP 2xx + `body.success = true` → `Result.Success(transform(body))`
- HTTP 2xx + `body.success = false` → `Result.Error(message)`
- HTTP 4xx/5xx → parse error body → `Result.Error(message)`
- Network exception (timeout, no internet) → `Result.Error("Connection Error: Unknown error")`

## Cách thêm API mới

1. Tạo Retrofit interface mới trong `data/remote/api/<TenAPI>.kt`.
2. Thêm `@Headers("No-Auth: true")` cho endpoint không cần auth.
3. Thêm `@Provides` trong `AppModule` (chú ý qualifier `@MainClient` hoặc `@AuthClient`).
4. Tạo Repository interface trong `domain/repository/`.
5. Implement Repository trong `data/repository/`, kế thừa `BaseRepository`.
6. Bind trong `RepositoryModule`.

## Anti-pattern

- ❌ Gọi API trực tiếp từ ViewModel (bypass Repository).
- ❌ Trả raw `Response<T>` của Retrofit lên Domain — domain phải nhận `Result<DomainModel>`.
- ❌ Dùng `runBlocking` trong Interceptor/Authenticator — đã có blocking API trong `TokenManager` (xem [token-management.md](token-management.md)).
- ❌ Chia sẻ cùng OkHttpClient cho cả authenticated path và refresh path.
