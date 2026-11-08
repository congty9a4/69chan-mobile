package com.mycom.chan.preview

import android.net.Uri
import com.mycom.chan.R
import com.mycom.chan.domain.model.Comment
import com.mycom.chan.domain.model.Info
import com.mycom.chan.domain.model.Media
import com.mycom.chan.domain.model.Notification
import com.mycom.chan.domain.model.Post
import com.mycom.chan.domain.model.Profile
import com.mycom.chan.domain.model.User
import com.mycom.chan.domain.model.UserSearchResult
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel
import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel
import com.mycom.chan.presentation.viewmodel.AuthState
import com.mycom.chan.presentation.viewmodel.NotificationState
import com.mycom.chan.presentation.viewmodel.ProfileState
import com.mycom.chan.presentation.viewmodel.SearchState
import java.time.Instant
import java.time.LocalDateTime

/**
 * Sample content for [PreviewActivity]. The images live in src/debug/res,
 * so the previews work offline and never reach the real backend.
 */
internal class PreviewData(private val packageName: String) {

    private fun res(id: Int) = "android.resource://$packageName/$id"

    /** ISO date-time [minutes] ago, the format the backend sends and formatRelativeTime() reads. */
    private fun ago(minutes: Long) = LocalDateTime.now().minusMinutes(minutes).withNano(0).toString()

    private val avatar = listOf(
        R.drawable.preview_avatar_1,
        R.drawable.preview_avatar_2,
        R.drawable.preview_avatar_3,
        R.drawable.preview_avatar_4,
        R.drawable.preview_avatar_5,
        R.drawable.preview_avatar_6
    ).map(::res)

    val me = User(id = "u1", userName = "minhanh", fullName = "Minh Anh", avatarUrl = avatar[0])
    val baoTram = User(id = "u2", userName = "baotram", fullName = "Bảo Trâm", avatarUrl = avatar[1])
    private val quocHuy = User(id = "u3", userName = "quochuy", fullName = "Quốc Huy", avatarUrl = avatar[2])
    private val linhDan = User(id = "u4", userName = "linhdan", fullName = "Linh Đan", avatarUrl = avatar[3])
    private val tuanKiet = User(id = "u5", userName = "tuankiet", fullName = "Tuấn Kiệt", avatarUrl = avatar[4])
    private val ngocPhuong = User(id = "u6", userName = "ngocphuong", fullName = "Ngọc Phương", avatarUrl = avatar[5])

    val authState = AuthState(isLoggedIn = true, currentUser = me, profileExists = true, isLoading = false)

    private fun photo(id: Int) = listOf(Media(url = res(id), type = "IMAGE", uploadedAt = Instant.now()))

    val posts = listOf(
        Post(
            id = "p1", tags = listOf("dalat"),
            caption = "Hoàng hôn ở Đà Lạt hôm qua, lạnh nhưng đáng 🌄",
            medias = photo(R.drawable.preview_post_1), user = baoTram, scope = "PUBLIC",
            likes = 128, comment = 14, isLiked = true
        ),
        Post(
            id = "p2", tags = emptyList(),
            caption = "Có ai biết quán cà phê nào yên tĩnh ở Quận 3 để ngồi code cả buổi chiều không?",
            medias = emptyList(), user = quocHuy, scope = "PUBLIC",
            likes = 23, comment = 31, isLiked = false
        ),
        Post(
            id = "p3", tags = listOf("bien"),
            caption = "Cuối tuần trốn ra biển một chút.",
            medias = photo(R.drawable.preview_post_2), user = linhDan, scope = "PUBLIC",
            likes = 342, comment = 27, isLiked = false
        ),
        Post(
            id = "p4", tags = emptyList(),
            caption = "Sài Gòn về đêm nhìn từ tầng 30.",
            medias = photo(R.drawable.preview_post_3), user = tuanKiet, scope = "PUBLIC",
            likes = 76, comment = 5, isLiked = false
        )
    )

    private fun info(user: User) = Info(id = user.id, username = user.userName, avatarUrl = user.avatarUrl)

    val notificationState = NotificationState(
        notifications = listOf(
            Notification("n1", info(baoTram), "LIKE_POST", "p1", "baotram liked your post", ago(3), read = false),
            Notification("n2", info(quocHuy), "COMMENT_POST", "p1", "quochuy commented: \"Đẹp quá, chỗ này ở đâu vậy?\"", ago(25), read = false),
            Notification("n3", info(tuanKiet), "FRIEND_REQUEST", null, "tuankiet sent you a friend request", ago(90), read = false),
            Notification("n4", info(linhDan), "ACCEPT_FRIEND", null, "linhdan accepted your friend request", ago(60 * 5), read = true),
            Notification("n5", info(ngocPhuong), "LIKE_POST", "p2", "ngocphuong and 12 others liked your post", ago(60 * 26), read = true),
            Notification("n6", info(quocHuy), "COMMENT_POST", "p3", "quochuy replied to your comment", ago(60 * 24 * 3), read = true)
        ),
        unreadCount = 3
    )

    val comments = listOf(
        Comment("c1", "Đẹp quá, chỗ này ở đâu vậy?", quocHuy, ago(42)),
        Comment("c2", "Đồi chè Cầu Đất đó, đi sớm tầm 5h là đẹp nhất.", baoTram, ago(38)),
        Comment("c3", "Lưu lại, tháng sau đi 🙌", linhDan, ago(20)),
        Comment("c4", "Ảnh chụp bằng máy gì vậy bạn?", tuanKiet, ago(9)),
        Comment("c5", "Điện thoại thôi, có chỉnh màu chút xíu.", baoTram, ago(2))
    )

    val chatItems = listOf(
        ChatListItemUiModel("conv1", baoTram.userName, baoTram.avatarUrl, "7h nha, mình đặt bàn 4 người rồi", "2m ago", 2),
        ChatListItemUiModel("conv2", quocHuy.userName, quocHuy.avatarUrl, "Ok để mình gửi file cho", "35m ago", 0),
        ChatListItemUiModel("conv3", linhDan.userName, linhDan.avatarUrl, "Haha đúng rồi 😂", "3h ago", 5),
        ChatListItemUiModel("conv4", tuanKiet.userName, tuanKiet.avatarUrl, "Cảm ơn nha!", "1d ago", 0),
        ChatListItemUiModel("conv5", ngocPhuong.userName, ngocPhuong.avatarUrl, "Hẹn thứ bảy nhé", "06/11", 0)
    )

    val messages = listOf(
        MessageUiModel(1, "Ê, tối nay rảnh không?", isMine = false, position = MessagePosition.TOP),
        MessageUiModel(2, "Có quán lẩu mới mở gần nhà mình, nghe nói ngon lắm", isMine = false, position = MessagePosition.BOTTOM),
        MessageUiModel(3, "Rảnh nè", isMine = true, position = MessagePosition.TOP),
        MessageUiModel(4, "Mấy giờ vậy?", isMine = true, position = MessagePosition.MIDDLE),
        MessageUiModel(5, "Rủ thêm Huy với Đan luôn đi", isMine = true, position = MessagePosition.BOTTOM),
        MessageUiModel(6, "Ok luôn", isMine = false, position = MessagePosition.TOP),
        MessageUiModel(7, "7h nha, mình đặt bàn 4 người rồi", isMine = false, position = MessagePosition.BOTTOM)
    )

    val searchState = SearchState(
        query = "minh",
        users = listOf(
            UserSearchResult("u1", "minhanh", "minhanh@example.com"),
            UserSearchResult("u7", "minhthu", "minhthu@example.com"),
            UserSearchResult("u8", "minhkhoa.dev", "minhkhoa@example.com")
        ),
        posts = listOf(posts[1])
    )

    private fun profileOf(user: User, bio: String, location: String, birthday: String) = Profile(
        id = user.id.drop(1).toInt(),
        bio = bio,
        location = location,
        birthday = birthday,
        phone = null,
        user = user,
        avatarUrl = user.avatarUrl,
        coverPhotoUrl = res(R.drawable.preview_cover),
        createdAt = ago(60 * 24 * 200),
        updatedAt = ago(60 * 24 * 3)
    )

    /** Someone else's profile: shows the Follow button. */
    val otherProfileState = ProfileState(
        profile = profileOf(
            baoTram,
            bio = "Thích chụp ảnh hoàng hôn và uống cà phê sữa đá. Đà Lạt là nhà thứ hai.",
            location = "TP. Hồ Chí Minh",
            birthday = "2001-06-28"
        ),
        isFollowing = false
    )

    /** Own profile, already in edit mode. */
    val ownProfileEditState = ProfileState(
        profile = profileOf(me, bio = "Dev Android, mê mèo.", location = "Hà Nội", birthday = "2002-03-15"),
        isEditing = true,
        bio = "Dev Android, mê mèo.",
        location = "Hà Nội",
        birthday = "2002-03-15",
        phone = "0901 234 567"
    )

    val draftText = "Cuối tuần này có ai đi Đà Lạt không? Mình còn dư một vé xe."
    val draftUris: List<Uri> = listOf(Uri.parse(res(R.drawable.preview_post_1)))
}
