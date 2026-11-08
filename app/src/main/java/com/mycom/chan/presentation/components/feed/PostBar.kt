package com.mycom.chan.presentation.components.feed

import android.icu.text.CompactDecimalFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing
import java.util.Locale

enum class ActionType {
    LIKE,
    COMMENT,
    SHARE,
    BOOKMARK
}

@Composable
fun PostBar(
    likes: Int = 0,
    comments: Int = 0,
    isLiked: Boolean = false,
    onLike: () -> Unit = {},
    onComment: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(ActionType.LIKE, count = likes, isActive = isLiked, onClick = onLike)
        ActionButton(ActionType.COMMENT, count = comments, onClick = onComment)
        ActionButton(ActionType.SHARE)
        ActionButton(ActionType.BOOKMARK)
    }
}

@Composable
fun ActionButton(
    type: ActionType,
    count: Int? = null,
    isActive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val format = remember {
        CompactDecimalFormat.getInstance(
            Locale.US,
            CompactDecimalFormat.CompactStyle.SHORT
        )
    }

    val iconRes = when (type) {
        ActionType.LIKE -> if (isActive) R.drawable.heart_filled else R.drawable.heart_blank
        ActionType.COMMENT -> R.drawable.comment
        ActionType.SHARE -> R.drawable.shared
        ActionType.BOOKMARK -> R.drawable.bookmark
    }
    val label = when (type) {
        ActionType.LIKE -> "Like"
        ActionType.COMMENT -> "Comment"
        ActionType.SHARE -> "Share"
        ActionType.BOOKMARK -> "Save"
    }
    val tint = if (type == ActionType.LIKE && isActive) ChanTheme.colors.like else LocalContentColor.current
    val countText = count?.let { format.format(it) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(role = Role.Button, onClick = onClick)
            // At least 48x48dp to tap, even though the icon is only 20dp
            .minimumInteractiveComponentSize()
            .padding(horizontal = Spacing.sm)
            // One announcement per button, e.g. "Like, 12, Liked"
            .clearAndSetSemantics {
                contentDescription = if (countText != null) "$label, $countText" else label
                role = Role.Button
                if (type == ActionType.LIKE) {
                    stateDescription = if (isActive) "Liked" else "Not liked"
                }
                onClick(label = label) { onClick(); true }
            }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(IconSize.small)
        )
        if (countText != null) {
            Text(
                text = countText,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
