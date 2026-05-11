package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R
import com.mycom.chan.presentation.components.Avatar

@Composable
fun ChatNavigationBar(
    senderName: String,
    senderAvatar: String?,
    onNavigateToChatList: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xFFdbdee3),
                    0.3f to Color(0xFFb3b6b7)
                ),
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = Float.POSITIVE_INFINITY, y = 150f)
            )
        )
//        .border(
//            width = 2.dp,
//            brush = Brush.linearGradient(
//                colorStops = arrayOf(
//                    0.0f to Color.White,
//                    0.3f to Color.Black
//                ),
//                start = Offset(0f,0f),
//                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
//            ),
//            shape = RectangleShape
//        )
        .heightIn(max = 100.dp, min = 70.dp)
        .statusBarsPadding()
        .padding(horizontal = 10.dp)

        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(
            onClick = onNavigateToChatList,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back),
                modifier = Modifier.size(30.dp),
                contentDescription = "back-to-chat-list"
            )
        }

        Row (
            modifier = Modifier.clickable(onClick = {}),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            Text(
                text = senderName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Avatar(
                modifier = Modifier.size(50.dp),
                imageUrl = senderAvatar,
                isEditable = false
            )
        }

    }

}