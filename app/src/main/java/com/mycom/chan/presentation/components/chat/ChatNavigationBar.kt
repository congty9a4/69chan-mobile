package com.mycom.chan.presentation.components.chat

import com.mycom.chan.presentation.components.BackButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.mycom.chan.R
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.Spacing

/** Conversation top bar: back button, then the other person's avatar and name. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatNavigationBar(
    senderName: String,
    senderAvatar: String?,
    onNavigateToChatList: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackButton(onClick = onNavigateToChatList, contentDescription = "Back to messages")
        },
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    modifier = Modifier.size(AvatarSize.medium),
                    imageUrl = senderAvatar
                )
                Text(
                    text = senderName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}
