package com.mycom.chan.presentation.page

import com.mycom.chan.presentation.components.BackButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.R
import com.mycom.chan.domain.model.Notification
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.util.formatRelativeTime
import com.mycom.chan.presentation.viewmodel.NotificationState
import com.mycom.chan.presentation.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notifications(
    onBack: () -> Unit,
    onNavigateToPost: (String) -> Unit = {},
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NotificationsContent(
        uiState = uiState,
        onBack = onBack,
        onMarkAllRead = viewModel::markAllAsRead,
        onNotificationClick = { notification ->
            if (!notification.read) viewModel.markAsRead(notification.id)
            notification.targetId?.let { onNavigateToPost(it) }
        }
    )
}

/** Notifications UI without the ViewModel, so the debug preview screen can show it with sample data. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationsContent(
    uiState: NotificationState,
    onBack: () -> Unit,
    onMarkAllRead: () -> Unit,
    onNotificationClick: (Notification) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    BackButton(onClick = onBack)
                },
                actions = {
                    if (uiState.notifications.any { !it.read }) {
                        TextButton(onClick = onMarkAllRead) {
                            Text("Mark all read")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.notifications.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    items(
                        items = uiState.notifications,
                        key = { it.id }
                    ) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = { onNotificationClick(notification) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val isUnread = !notification.read
    val bgColor = if (isUnread) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable(onClick = onClick)
            // Unread is shown by the tint, the dot and bold text; this says it out loud too
            .semantics(mergeDescendants = true) {
                if (isUnread) stateDescription = "Unread"
            }
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        verticalAlignment = Alignment.Top
    ) {
        Avatar(
            imageUrl = notification.sender?.avatarUrl,
            modifier = Modifier.size(AvatarSize.large)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                NotificationTypeBadge(type = notification.type)
                if (isUnread) {
                    Box(
                        modifier = Modifier
                            .size(Spacing.sm)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Normal
            )
            Text(
                text = formatRelativeTime(notification.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotificationTypeBadge(type: String) {
    val colors = ChanTheme.colors
    val (label, color) = when (type.uppercase()) {
        "LIKE_POST"      -> "Like"           to colors.like
        "COMMENT_POST"   -> "Comment"        to colors.comment
        "FRIEND_REQUEST" -> "Friend request" to colors.friendRequest
        "ACCEPT_FRIEND"  -> "Accepted"       to colors.friendAccepted
        else             -> type             to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xxs)
        )
    }
}
