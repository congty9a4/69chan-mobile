package com.mycom.chan.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.mycom.chan.R

@Composable
fun Avatar(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.blank_profile_picture),
        contentDescription = "user-profile-picture",
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}