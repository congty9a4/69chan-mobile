package com.mycom.chan.presentation.components.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun PostContent(
    postStatus: String? = null,
    postContent: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        if (postStatus != null) {
            Text(
                text = postStatus,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (postContent != null) {
            AsyncImage(
                model = postContent,
                contentDescription = "Image attached to the post",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(MaterialTheme.shapes.medium)
                    // Visible while the image loads, and if it fails
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }
    }
}
