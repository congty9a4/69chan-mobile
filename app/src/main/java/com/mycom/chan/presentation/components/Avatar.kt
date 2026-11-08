package com.mycom.chan.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mycom.chan.R

/**
 * Round user avatar.
 *
 * Pass [onClick] only when tapping the avatar does something; otherwise it is not clickable
 * and has no ripple. Leave [contentDescription] null when the user's name is shown next to it,
 * so screen readers do not read the same person twice.
 */
@Composable
fun Avatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    isEditable: Boolean = false,
    onClickLabel: String? = null,
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(onClickLabel = onClickLabel, role = Role.Button, onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .then(clickModifier)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        ) {
            val state = painter.state
            if (
                state is AsyncImagePainter.State.Loading ||
                state is AsyncImagePainter.State.Error ||
                imageUrl.isNullOrEmpty()
            ) {
                // Shown while loading, on error, or when the user has no avatar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.blank_profile_picture),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxSize(0.6f)
                    )
                }
            } else {
                SubcomposeAsyncImageContent()
            }
        }

        if (isEditable) {
            // Scrim over a photo: fixed black/white on purpose, it must read the same in both themes
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.edit),
                    contentDescription = "Edit avatar",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize(0.4f)
                )
            }
        }
    }
}
