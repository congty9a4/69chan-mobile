package com.mycom.chan.presentation.components

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mycom.chan.R

@Composable
fun Avatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
        ) {
            val state = painter.state
            if (
                state is AsyncImagePainter.State.Loading ||
                state is AsyncImagePainter.State.Error ||
                imageUrl.isNullOrEmpty()
                ) {
                // Show this while the image is loading, if it fails, or if the URL is null
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.blank_profile_picture),
                        contentDescription = "Placeholder",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                // On success, show the actual image
                SubcomposeAsyncImageContent()
            }
        }

        if (isEditable) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.edit),
                    contentDescription = "Edit Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
