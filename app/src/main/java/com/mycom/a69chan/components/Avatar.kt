package com.mycom.a69chan.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycom.a69chan.R

@Composable
fun Avatar() {
    Image(
        painter = painterResource(id = R.drawable.blank_profile_picture),
        contentDescription = "user-profile-picture",
        modifier = Modifier.size(37.dp).clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}