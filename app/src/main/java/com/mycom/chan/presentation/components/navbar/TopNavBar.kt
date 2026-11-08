package com.mycom.chan.presentation.components.navbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.IconSize

/** Feed top bar: menu button on the left, logo in the middle. Handles the status bar inset. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(onMenuClick: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.menu),
                    contentDescription = "Open menu",
                    modifier = Modifier.size(IconSize.medium)
                )
            }
        },
        title = {
            // The logo is not a button, so it is a plain image rather than an IconButton
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.logo),
                contentDescription = "69chan",
                modifier = Modifier.size(32.dp)
            )
        }
    )
}
