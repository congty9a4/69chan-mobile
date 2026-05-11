# Token management

Mô tả cách lưu trữ access token + refresh token, cơ chế reactive cho UI, và pattern dual-API (suspend + blocking).

## Mục tiêu

1. Lưu token an toàn, persistent qua restart app.
2. UI tự động cập nhật khi token đổi (login/logout).
3. Cho phép cả coroutine code (ViewModel) lẫn sync code (Interceptor) đọc/ghi token mà không cần `runBlocking`.

## Storage backend: Android `AccountManager`

Project dùng `AccountManager` của Android thay vì SharedPreferences/DataStore. Lý do:
- Token được hệ thống bảo vệ ở mức user-account.
- Tích hợp được với Settings → Accounts.
- Tách biệt user data với dữ liệu app khác.

Cấu hình account type qua [authenticator.xml](../../app/src/main/res/xml/authenticator.xml) + service đăng ký trong [AndroidManifest.xml](../../app/src/main/AndroidManifest.xml):

```xml
<service
    android:name=".data.local.auth.AuthenticatorService"
    android:exported="false">
    <intent-filter>
        <action android:name="android.accounts.AccountAuthenticator" />
    </intent-filter>
    <meta-data
        android:name="android.accounts.AccountAuthenticator"
        android:resource="@xml/authenticator" />
</service>
```

[AccountAuthenticator.kt](../../app/src/main/java/com/mycom/chan/data/local/auth/AccountAuthenticator.kt) là `AbstractAccountAuthenticator` placeholder — app tự quản lý token thủ công, không dùng OAuth flow của Android.

## Constants

```kotlin
const val ACCOUNT_TYPE = "com.mycom.chan"     // Phân biệt với Facebook, X, ...
const val AUTH_TOKEN_TYPE = "Bearer"           // Tag cho access token
const val REFRESH_TOKEN = "refresh_token"      // Key trong userData
const val KEY_USER = "user_data"               // Key cho JSON user
```

Đặt trong companion của [TokenManagerImple.kt](../../app/src/main/java/com/mycom/chan/data/local/auth/TokenManagerImple.kt).

## Interface

[TokenManager.kt](../../app/src/main/java/com/mycom/chan/domain/repository/TokenManager.kt) expose 2 nhóm API:

```kotlin
interface TokenManager {
    // Suspend APIs — cho ViewModel, Repository
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun observeAccessToken(): Flow<String?>
    suspend fun clearToken()
    suspend fun saveUser(user: String)
    suspend fun getUser(): String?

    // Blocking APIs — cho Interceptor, Authenticator (sync context)
    fun getAccessTokenBlocking(): String?
    fun getRefreshTokenBlocking(): String?
    // (Sắp thêm) saveAccessTokenBlocking, saveRefreshTokenBlocking, clearTokenBlocking
}
```

### Tại sao dual API?

OkHttp `Interceptor.intercept()` và `Authenticator.authenticate()` là **sync method** — không thể `suspend`. Nếu chỉ có suspend API, buộc dùng `runBlocking { ... }` — anti-pattern (xem [user_architecture_principles](../../user_architecture_principles.md), nguyên tắc 1).

Giải pháp: expose blocking API (chạy đồng bộ trên thread hiện tại) cho sync caller. AccountManager API vốn là sync nên không tốn gì thêm.

## Reactive state — `_accessTokenFlow`

[TokenManagerImple](../../app/src/main/java/com/mycom/chan/data/local/auth/TokenManagerImple.kt) dùng `MutableStateFlow<String?>` làm **single source of truth** cho access token:

```kotlin
private val _accessTokenFlow = MutableStateFlow<String?>(null)

init {
    _accessTokenFlow.value = readAccessTokenFromAccountManager()  // load lúc khởi tạo
}

override suspend fun saveAccessToken(accessToken: String) = withContext(Dispatchers.IO) {
    // ... lưu vào AccountManager ...
    _accessTokenFlow.value = accessToken           // emit cho observer
}

override suspend fun clearToken() = withContext(Dispatchers.IO) {
    // ... xóa khỏi AccountManager ...
    _accessTokenFlow.value = null                  // emit null
}

override fun getAccessTokenBlocking(): String? = _accessTokenFlow.value
override suspend fun getAccessToken(): String? = _accessTokenFlow.value
override suspend fun observeAccessToken(): Flow<String?> = _accessTokenFlow.asStateFlow()
```

### Tại sao tự dùng `MutableStateFlow` thay vì `OnAccountsUpdateListener`?

Listener của `AccountManager` chỉ fire khi account **add/update/remove**, không fire khi `setAuthToken()`. Nếu trông chờ nó để UI update sau login → **không bao giờ fire**. Tự control emit qua `MutableStateFlow` đảm bảo:
- Save token → emit ngay → UI update.
- Clear token → emit null → UI chuyển về "chưa đăng nhập".

## Refresh token cache

Refresh token không cần reactive (không có UI quan sát). Lưu cache đơn giản qua `@Volatile`:

```kotlin
@Volatile private var cachedRefreshToken: String? = null

override fun getRefreshTokenBlocking(): String? {
    cachedRefreshToken?.let { return it }
    val account = getCurrentAccountSync() ?: return null
    return accountManager.getUserData(account, REFRESH_TOKEN)
        ?.also { cachedRefreshToken = it }
}
```

Lazy load lần đầu, sau đó đọc từ memory. `@Volatile` đảm bảo visibility cho thread khác.

## Token refresh flow

Khi server trả 401:

```
[Request 401]
    │
    ▼
TokenAuthenticator.authenticate()              [sync, OkHttp dispatcher thread]
    │
    ├─ tokenRefresher.refreshTokenBlocking()
    │     │
    │     ├─ synchronized(refreshLock) {
    │     │   1. Double-check: token đã đổi rồi? → return true ngay
    │     │   2. Đọc refresh token (sync)
    │     │   3. Gọi authApi.refreshTokenSync().execute()    [qua @AuthClient]
    │     │   4. Save token mới (sync) → _accessTokenFlow emit
    │     │   5. Return true
    │     ├─ }
    │     │
    │     └─ catch network error → return false (giữ token cũ, user retry)
    │
    └─ Build new Request với token mới → OkHttp retry
```

> **Trạng thái 🚧:** `refreshTokenBlocking()` đang được implement. Hiện `TokenAuthenticatorImple` đã gọi method này nhưng `TokenRefresherImple` chưa có → project đang không build được. Xem [auth.md](../features/auth.md#known-issues) hoặc lịch sử PR.

## Lifecycle của token

```
App khởi động
    │
    ▼
TokenManagerImple constructor
    │
    ├─ init { _accessTokenFlow.value = readFromAccountManager() }
    │      └─ Có token cũ → load
    │      └─ Chưa có → null
    │
    ▼
AuthViewModel.init { observeAuthFlow() }
    │
    ├─ collect _accessTokenFlow
    │      └─ null → AuthState(isLoggedIn = false)
    │      └─ token → AuthState(isLoggedIn = true, currentUser = ...)
    │
    ▼
UI render dựa trên AuthState
```

## Anti-pattern

- ❌ `runBlocking { tokenManager.getAccessToken() }` trong sync method — dùng `getAccessTokenBlocking()`.
- ❌ Đọc thẳng từ `accountManager.peekAuthToken()` ngoài `TokenManagerImple` — bypass cache + flow.
- ❌ Lưu token vào SharedPreferences song song — tạo 2 source of truth.
- ❌ Update `_accessTokenFlow.value` trực tiếp từ ngoài class.

## Mở rộng — thêm trường lưu trữ mới

Ví dụ thêm `userId` cho dễ truy cập:

1. Thêm constant `KEY_USER_ID = "user_id"` trong `TokenManagerImple.companion`.
2. Thêm method vào `TokenManager` interface:
   ```kotlin
   suspend fun saveUserId(userId: String)
   suspend fun getUserId(): String?
   ```
3. Implement trong `TokenManagerImple`, dùng `accountManager.setUserData(account, KEY_USER_ID, userId)`.
4. Nếu cần reactive (UI quan sát thay đổi userId) → thêm `_userIdFlow: MutableStateFlow<String?>` tương tự access token.
5. Nếu cần sync getter cho Interceptor → thêm `getUserIdBlocking()`.
