package com.mycom.chan.presentation.components.navbar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun NavigationButton (id: Int,
                      contentDescription: String,
                      modifier: Modifier = Modifier,
                      size: Int,
                      onClick:() -> Unit) {
    IconButton(onClick = onClick,
        modifier = modifier.size(50.dp)) {
        Icon(imageVector = ImageVector.vectorResource(id),
            contentDescription = contentDescription,
            modifier = Modifier.size(size.dp)
        )
    }
}