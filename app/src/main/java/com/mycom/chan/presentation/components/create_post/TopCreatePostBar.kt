package com.mycom.chan.presentation.components.create_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.mycom.chan.domain.model.User
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun TopCreatePostBar(
    onCancelPost: () -> Unit,
    onCreatePost: () -> Unit,
    isPostAble: Boolean,
    user: User
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.sm, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        TextButton(onClick = onCancelPost) {
            Text("Cancel")
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics(mergeDescendants = true) { heading() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "New post",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "@${user.userName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        // Default colors: the disabled state is greyed out by Material,
        // instead of white text on a pale blue that could barely be read
        Button(
            onClick = onCreatePost,
            enabled = isPostAble
        ) {
            Text("Post")
        }
    }
}
