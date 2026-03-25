package com.mycom.chan.presentation.components.post


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Post(postStatus:String?=null,postContent:String?=null) {
    Column (modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .padding(
            vertical = 5.dp,
            horizontal = 5.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        PostHeader()
        PostContent(postStatus,postContent)
        PostBar()
        }

}
