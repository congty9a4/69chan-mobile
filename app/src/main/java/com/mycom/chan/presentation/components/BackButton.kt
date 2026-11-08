package com.mycom.chan.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.IconSize

/**
 * Back arrow for top bars. The drawable is declared at 800dp, so the icon size must be set
 * here or it grows to fill the whole button.
 */
@Composable
fun BackButton(
    onClick: () -> Unit,
    contentDescription: String = "Back"
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.back),
            contentDescription = contentDescription,
            modifier = Modifier.size(IconSize.medium)
        )
    }
}
