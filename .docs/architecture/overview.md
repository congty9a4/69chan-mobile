# Clean Architecture trong project

Project áp dụng Clean Architecture với 3 layer chính. Mục tiêu: đổi UI hoặc đổi backend không phải đụng business logic.

## Sơ đồ phụ thuộc

```
                    ┌──────────────────────┐
                    │     presentation     │     ← Compose UI, ViewModel
                    │  (Compose, ViewModel)│
                    └──────────┬───────────┘
                               │ depends on
                               ▼
                    ┌──────────────────────┐
                    │       domain         │     ← Pure Kotlin, không framework
                    │ (interfaces, models) │
                    └──────────▲───────────┘
                               │ implements
                    ┌──────────┴───────────┐
                    │        data          │     ← Retrofit, AccountManager
                    │  (API, DTO, repo)    │
                    └──────────────────────┘
```

**Quy tắc vàng:** mũi tên phụ thuộc **chỉ chỉ vào domain**.
- `data` implements interface trong `domain`.
- `presentation` dùng interface trong `domain`.
- `domain` không biết gì về `data` hay `presentation`.

## Cấu trúc thư mục

```
app/src/main/java/com/mycom/chan/
├── core/network/auth/          # Cross-cutting: token authenticator, refresher
├── data/
│   ├── local/auth/             # AccountManager-based token storage
│   ├── mapper/                 # DTO → Domain model
│   ├── remote/
│   │   ├── api/                # Retrofit interfaces
│   │   ├── dto/                # Request/response DTOs
│   │   └── interceptor/        # OkHttp interceptors
│   └── repository/             # Repository implementations (Imple*)
├── domain/
│   ├── model/                  # Domain models (User, Post, Authentication, ...)
│   └── repository/             # Repository interfaces (AuthRepository, ...)
├── presentation/
│   ├── components/             # Compose components dùng lại
│   ├── navigation/             # NavHost + Screen sealed class
│   ├── page/                   # Màn hình (Login, Register, Feed, ChatList, ...)
│   ├── theme/                  # Color, Typography, Theme
│   ├── uimodel/                # UI-specific models (MessageUiModel, ...)
│   └── viewmodel/              # @HiltViewModel classes
├── di/                         # Hilt modules (AppModule, RepositoryModule)
├── Main.kt                     # @HiltAndroidApp
└── MainActivity.kt             # @AndroidEntryPoint, setContent
```

## Quy ước import — kiểm tra layer purity

Mở file bất kỳ trong `domain/`, kiểm tra phần `import`:

| Dấu hiệu | Đánh giá |
|---|---|
| `import kotlinx.coroutines.flow.Flow` | ✅ OK — coroutine là utility ngôn ngữ |
| `import com.mycom.chan.domain.model.*` | ✅ OK — cùng domain |
| `import okhttp3.*` | ❌ Vi phạm — domain không nên biết HTTP |
| `import retrofit2.*` | ❌ Vi phạm |
| `import androidx.compose.*` | ❌ Vi phạm |
| `import com.google.gson.*` | ❌ Vi phạm |
| `import com.mycom.chan.data.remote.dto.*` | ⚠️ Vi phạm — domain không phụ thuộc data DTO |

## DI và scoping

Hilt được dùng để inject. 2 module chính:

- [AppModule.kt](../../app/src/main/java/com/mycom/chan/di/AppModule.kt) — provide singleton-level dependency: `Gson`, `OkHttpClient` (2 instance: `@MainClient` + `@AuthClient`), `Retrofit`, `AuthAPI`, `PostAPI`.
- [RepositoryModule.kt](../../app/src/main/java/com/mycom/chan/di/RepositoryModule.kt) — bind interface (`AuthRepository`, `TokenManager`, `TokenAuthenticator`, `TokenRefresher`) với implementation tương ứng.

**Nguyên tắc scope:**
- Service có state hệ thống (token, auth, network) → `@Singleton`.
- ViewModel → `@HiltViewModel` (Hilt tự scope theo `NavBackStackEntry` mặc định).
- Khi nhiều màn hình cần share VM (ví dụ AuthViewModel) → dùng `CompositionLocalProvider` ở root, không gọi `hiltViewModel()` mỗi nơi (sẽ ra instance riêng).

## State management

- **Repository → ViewModel:** `Result<T>` (sealed class) cho các call một lần (login, fetch).
- **Repository → ViewModel (state liên tục):** `Flow<T>` (e.g., `observeLoginState()`).
- **ViewModel → UI:** `StateFlow<UiState>` cho state, `Channel<Event>` cho event 1 lần (snackbar, navigation).

## Quy ước đặt tên

| Loại | Pattern | Ví dụ |
|---|---|---|
| Domain interface | Tên use case thuần | `AuthRepository`, `TokenManager` |
| Domain model | Danh từ thuần | `User`, `Post`, `Authentication` |
| DTO | `XxxDTO` hoặc tên rõ + `Request`/`Response` | `UserDTO`, `LoginRequest`, `AuthResponseDTO` |
| Repository impl | `Imple<Domain>` | `ImpleAuthRepository`, `ImplePostRepository` |
| ViewModel | `<Feature>ViewModel` | `LoginViewModel`, `AuthViewModel` |
| UI state | `<Feature>State` | `LoginState`, `AuthState` |
| UI event | `<Feature>Event` (sealed) | `LoginEvent.ShowError` |

> **Lưu ý:** Pattern `Imple<X>` là đặc thù của project này. Convention chuẩn cộng đồng thường là `XxxImpl` hoặc `DefaultXxx`. Nếu sau này refactor, đổi đồng loạt.

## Mở rộng

Khi thêm tính năng mới:

1. **Domain trước** — định nghĩa model + interface trong `domain/`.
2. **Data sau** — implement interface, thêm DTO + mapper.
3. **DI** — bind trong `RepositoryModule`.
4. **Presentation cuối** — ViewModel + Composable.

Đi từ trong ra ngoài giúp domain không bị nhiễm framework.
