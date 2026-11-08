package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel

@Composable
fun ChatListItem(
    item: ChatListItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasUnread = item.unreadMessageCount > 0

    ListItem(
        modifier = modifier.clickable(onClickLabel = "Open conversation", onClick = onClick),
        leadingContent = {
            Avatar(
                modifier = Modifier.size(AvatarSize.large),
                imageUrl = item.senderAvatar
            )
        },
        headlineContent = {
            Text(
                text = item.senderUsername,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            // Unread conversations use bolder, darker text as well as the badge,
            // so the state is not shown by color alone
            Text(
                text = item.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Normal,
                color = if (hasUnread) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    text = item.lastMessageTimestamp,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (hasUnread) {
                    Badge(
                        modifier = Modifier.semantics {
                            contentDescription = "${item.unreadMessageCount} unread messages"
                        }
                    ) {
                        Text(if (item.unreadMessageCount > 99) "99+" else item.unreadMessageCount.toString())
                    }
                }
            }
        }
    )
}
