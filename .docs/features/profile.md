# Tính năng: Profile

Lấy thông tin profile (bio, avatar, cover, location, birthday) của user.

## Trạng thái

| Sub-feature | Trạng thái |
|---|---|
| Get own profile (sau login) | ✅ Phase 1.1 |
| View profile page (chi tiết) | 🚧 Phase 1.2 |
| View profile of others | 🚧 Phase 1.3 |
| Edit profile | 🚧 Phase 1.4 |
| Create profile (sau register) | 🚧 Phase 1.5 |

## API endpoints

| Method | Endpoint | Mô tả | Trạng thái |
|---|---|---|---|
| GET | `/users/{userId}/profile` | Lấy profile theo userId | ✅ |
| GET | `/profiles/{id}` | Lấy profile theo profile ID | ✅ |
| GET | `/users/{userId}/profile/exists` | Check profile tồn tại | ⏭️ |
| POST | `/profiles` | Tạo profile | ⏭️ |
| PUT | `/profiles/{id}` | Update profile | ⏭️ |
| GET | `/profiles/check-keyname` | Check keyname unique | ⏭️ |

## Schema (Profile từ API)

`id, bio, location, birthday, phone, user (Infochan), avatar_url, cover_photo_url, created_at, updated_at`

**Lưu ý:** API không trả `follower_count` / `following_count` / `post_count`. Sẽ implement ở Phase 5 (Follow) hoặc khi backend thêm.

## Component

- Domain: `Profile.kt`, `ProfileRepository.kt`
- Data: `ProfileDTO.kt`, `ProfileAPI.kt`, `ProfileMapper.kt`, `ProfileRepositoryImple.kt`
- DI: `AppModule.provideProfileAPI`, `RepositoryModule.bindProfileRepository`
- Presentation: profile fetched trong `AuthViewModel`, dùng trong `Menu.kt` Header