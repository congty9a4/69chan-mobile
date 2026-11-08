package com.mycom.chan.presentation.components.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.Ledger
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun PostHeader(
    avatarUrl: String = "",
    userName: String = "Username",
    createdAt: String = "",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Avatar(
            imageUrl = avatarUrl,
            modifier = Modifier.size(AvatarSize.medium)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleSmall,
                fontFamily = Ledger,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (createdAt.isNotBlank()) {
                Text(
                    text = createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
