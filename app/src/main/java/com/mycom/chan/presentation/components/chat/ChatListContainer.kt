package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel

@Composable
fun ChatListContainer(
    modifier: Modifier,
    chatItems: List<ChatListItemUiModel>,
    onChatItemClick: (String) -> Unit,
) {
    if (chatItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Spacing.xl),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No conversations yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = Spacing.sm)
    ) {
        items(
            items = chatItems,
            key = { it.conversationId }
        ) { item ->
            ChatListItem(
                item = item,
                onClick = { onChatItemClick(item.conversationId) }
            )
            // Inset divider that starts after the avatar, like most messaging apps
            HorizontalDivider(
                modifier = Modifier.padding(start = Spacing.lg + AvatarSize.large + Spacing.lg)
            )
        }
    }
}
