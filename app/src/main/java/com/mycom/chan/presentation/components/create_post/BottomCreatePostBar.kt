package com.mycom.chan.presentation.components.create_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycom.chan.R

@Composable
fun BottomCreatePostBar (
    onAddImage: () -> Unit,
    onAddVideo: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        IconButton(
            onClick = onAddImage
        ) {
            Icon(
            imageVector = ImageVector.vectorResource(R.drawable.image),
            contentDescription = "add-image-to-post",
            tint = Color(0xff4C9EEB)
            )
        }

        IconButton(
            onClick = onAddVideo
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.video),
                contentDescription = "add-video-to-post",
                tint = Color(0xff4C9EEB)
            )

        }
    }
}