package com.mycom.chan.presentation.components.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PostContent (postStatus:String?=null, postContent:String?=null) {
    Column {
        if (postStatus != null) {
            Text(
                text = postStatus,
                fontSize = 16.sp,
                lineHeight = 21.sp,
                letterSpacing = -(0.3).sp
            )
        }
        if (postContent != null) {
            AsyncImage(
                model = postContent,
                contentDescription = "post-image-or-video",
                modifier = Modifier.fillMaxWidth().aspectRatio(2f)
//                placeholder = TODO(),
//                error = TODO(),
//                fallback = TODO(),
//                onLoading = TODO(),
//                onSuccess = TODO(),
//                onError = TODO(),
            )
        }
    }
}