package com.mycom.chan.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mycom.chan.domain.model.Comment
import com.mycom.chan.domain.model.Post
import com.mycom.chan.presentation.page.ChatContent
import com.mycom.chan.presentation.page.ChatListContent
import com.mycom.chan.presentation.page.CommentsContent
import com.mycom.chan.presentation.page.CreatePost
import com.mycom.chan.presentation.page.CreateProfileContent
import com.mycom.chan.presentation.page.FeedContent
import com.mycom.chan.presentation.page.NotificationsContent
import com.mycom.chan.presentation.page.ProfileContent
import com.mycom.chan.presentation.page.SearchContent
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.uimodel.ChatFilter
import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel
import com.mycom.chan.presentation.viewmodel.ChatState
import com.mycom.chan.presentation.viewmodel.CommentsState
import com.mycom.chan.presentation.viewmodel.FeedState
import java.time.LocalDateTime

/**
 * Debug builds only: every logged-in screen drawn with sample data from [PreviewData],
 * without an account, the backend or any ViewModel. Buttons work locally (like, follow,
 * typing, sending) and nothing leaves the device.
 *
 * Open it from the "69chan previews" launcher icon, or jump straight to one screen:
 *
 *     adb shell am start -n com.mycom.chan/com.mycom.chan.preview.PreviewActivity --es screen feed
 *
 * Screen keys are listed in [PreviewScreen]. The theme follows the system light/dark setting.
 */
class PreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val start = intent.getStringExtra(EXTRA_SCREEN)?.let { key ->
            PreviewScreen.entries.firstOrNull { it.key == key }
        }
        val data = PreviewData(packageName)

        setContent {
            ChanTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val stack = remember { mutableStateListOf<PreviewScreen>().apply { start?.let(::add) } }
                    val current = stack.lastOrNull()

                    BackHandler(enabled = current != null) { stack.removeAt(stack.lastIndex) }

                    if (current == null) {
                        PreviewCatalog(onOpen = { stack.add(it) })
                    } else {
                        key(current) {
                            PreviewScreenHost(
                                screen = current,
                                data = data,
                                open = { stack.add(it) },
                                back = { stack.removeAt(stack.lastIndex) }
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_SCREEN = "screen"
    }
}

internal enum class PreviewScreen(val key: String, val title: String, val description: String) {
    FEED("feed", "Feed", "Home timeline with photo and text posts"),
    MENU("menu", "Menu", "Feed with the side drawer open"),
    SEARCH("search", "Search", "Results for a query"),
    NOTIFICATIONS("notifications", "Notifications", "Unread and read items"),
    CHAT_LIST("chat_list", "Messages", "Conversation list"),
    CHAT("chat", "Chat", "One conversation"),
    COMMENTS("comments", "Comments", "Comments on a post"),
    PROFILE("profile", "Profile", "Someone else's profile"),
    PROFILE_EDIT("profile_edit", "Edit profile", "Own profile in edit mode"),
    CREATE_POST("create_post", "New post", "Writing a post with a photo"),
    CREATE_PROFILE("create_profile", "Profile setup", "First-time profile form")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewCatalog(onOpen: (PreviewScreen) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Screen previews")
                        Text(
                            text = "Sample data · debug builds only",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(PreviewScreen.entries) { screen ->
                ListItem(
                    headlineContent = { Text(screen.title) },
                    supportingContent = { Text(screen.description) },
                    modifier = Modifier.clickable { onOpen(screen) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun PreviewScreenHost(
    screen: PreviewScreen,
    data: PreviewData,
    open: (PreviewScreen) -> Unit,
    back: () -> Unit
) {
    when (screen) {
        PreviewScreen.FEED, PreviewScreen.MENU -> {
            var feed by remember { mutableStateOf(FeedState(posts = data.posts)) }
            FeedContent(
                uiState = feed,
                authState = data.authState,
                unreadNotificationCount = data.notificationState.unreadCount,
                onLike = { id -> feed = feed.copy(posts = feed.posts.map { if (it.id == id) it.toggledLike() else it }) },
                onLoadMore = {},
                onLogout = back,
                onNavigateToComments = { open(PreviewScreen.COMMENTS) },
                onNavigateToCreatePost = { open(PreviewScreen.CREATE_POST) },
                onNavigateToProfile = { open(PreviewScreen.PROFILE_EDIT) },
                onNavigateToSearch = { open(PreviewScreen.SEARCH) },
                onNavigateToNotifications = { open(PreviewScreen.NOTIFICATIONS) },
                onNavigateToMessages = { open(PreviewScreen.CHAT_LIST) },
                drawerState = rememberDrawerState(
                    if (screen == PreviewScreen.MENU) DrawerValue.Open else DrawerValue.Closed
                )
            )
        }

        PreviewScreen.SEARCH -> {
            var search by remember { mutableStateOf(data.searchState) }
            SearchContent(
                uiState = search,
                onBack = back,
                onQueryChange = { search = search.copy(query = it) },
                onFilterChange = { search = search.copy(filter = it) },
                onNavigateToProfile = { open(PreviewScreen.PROFILE) }
            )
        }

        PreviewScreen.NOTIFICATIONS -> {
            var state by remember { mutableStateOf(data.notificationState) }
            NotificationsContent(
                uiState = state,
                onBack = back,
                onMarkAllRead = {
                    state = state.copy(notifications = state.notifications.map { it.copy(read = true) }, unreadCount = 0)
                },
                onNotificationClick = { clicked ->
                    state = state.copy(
                        notifications = state.notifications.map { if (it.id == clicked.id) it.copy(read = true) else it }
                    )
                }
            )
        }

        PreviewScreen.CHAT_LIST -> {
            var filter by remember { mutableStateOf(ChatFilter.ALL) }
            ChatListContent(
                isLoading = false,
                filter = filter,
                chatItems = if (filter == ChatFilter.UNREAD) {
                    data.chatItems.filter { it.unreadMessageCount > 0 }
                } else {
                    data.chatItems
                },
                onBack = back,
                onFilterSelected = { filter = it },
                onChatItemClick = { open(PreviewScreen.CHAT) }
            )
        }

        PreviewScreen.CHAT -> {
            var chat by remember { mutableStateOf(ChatState(messages = data.messages)) }
            ChatContent(
                receiverName = data.baoTram.fullName,
                receiverAvatar = data.baoTram.avatarUrl,
                uiState = chat,
                onBack = back,
                onInputChange = { chat = chat.copy(inputText = it) },
                onSend = {
                    val sent = MessageUiModel(
                        id = chat.messages.size + 1L,
                        content = chat.inputText.trim(),
                        isMine = true,
                        position = MessagePosition.SINGLE
                    )
                    chat = chat.copy(messages = chat.messages + sent, inputText = "")
                }
            )
        }

        PreviewScreen.COMMENTS -> {
            var comments by remember { mutableStateOf(CommentsState(comments = data.comments)) }
            CommentsContent(
                uiState = comments,
                onBack = back,
                onInputChange = { comments = comments.copy(inputText = it) },
                onSend = {
                    val mine = Comment(
                        id = "new${comments.comments.size}",
                        content = comments.inputText.trim(),
                        user = data.me,
                        createdAt = LocalDateTime.now().withNano(0).toString()
                    )
                    comments = comments.copy(comments = comments.comments + mine, inputText = "")
                }
            )
        }

        PreviewScreen.PROFILE, PreviewScreen.PROFILE_EDIT -> {
            val isOwn = screen == PreviewScreen.PROFILE_EDIT
            var profile by remember {
                mutableStateOf(if (isOwn) data.ownProfileEditState else data.otherProfileState)
            }
            ProfileContent(
                uiState = profile,
                isOwnProfile = isOwn,
                onBack = back,
                onStartEditing = { profile = profile.copy(isEditing = true) },
                onFollow = { profile = profile.copy(isFollowing = !profile.isFollowing) },
                onBioChange = { profile = profile.copy(bio = it) },
                onLocationChange = { profile = profile.copy(location = it) },
                onBirthdayChange = { profile = profile.copy(birthday = it) },
                onPhoneChange = { profile = profile.copy(phone = it) },
                onCancelEditing = { profile = profile.copy(isEditing = false) },
                onSave = {
                    profile = profile.copy(
                        isEditing = false,
                        profile = profile.profile?.copy(
                            bio = profile.bio,
                            location = profile.location,
                            birthday = profile.birthday,
                            phone = profile.phone
                        )
                    )
                }
            )
        }

        PreviewScreen.CREATE_POST -> {
            var text by remember { mutableStateOf(data.draftText) }
            CreatePost(
                onCancelPost = back,
                onCreatePost = back,
                isPostAble = text.isNotBlank(),
                user = data.me,
                onAddImage = {},
                onAddVideo = {},
                text = text,
                onTextChange = { text = it },
                selectedUris = data.draftUris
            )
        }

        PreviewScreen.CREATE_PROFILE -> {
            CreateProfileContent(
                isSaving = false,
                snackbarHostState = remember { SnackbarHostState() },
                onSubmit = { _, _, _, _ -> },
                onBackToLogin = back
            )
        }
    }
}

private fun Post.toggledLike() = copy(isLiked = !isLiked, likes = likes + if (isLiked) -1 else 1)
