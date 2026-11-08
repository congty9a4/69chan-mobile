package com.mycom.chan.presentation.components.create_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun BottomCreatePostBar(
    onAddImage: () -> Unit,
    onAddVideo: () -> Unit
) {
    Column {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            IconButton(onClick = onAddImage) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.image),
                    contentDescription = "Add image",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(IconSize.medium)
                )
            }
            IconButton(onClick = onAddVideo) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.video),
                    contentDescription = "Add video",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(IconSize.medium)
                )
            }
        }
    }
}
