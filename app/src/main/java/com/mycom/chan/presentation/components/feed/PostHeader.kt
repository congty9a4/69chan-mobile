package com.mycom.chan.presentation.components.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mycom.chan.presentation.theme.Gray
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.theme.Ledger

@Composable
fun PostHeader (
    avatarUrl: String = "",
    userName: String = "Username",
    createdAt: String = ""
) {
    Row (modifier = Modifier
        .width(280.dp)
        .padding(
            vertical = 2.dp,
            horizontal = 5.dp)) {
        Avatar(modifier = Modifier.size(48.dp),
            imageUrl = avatarUrl,
            isEditable = false)
        Column(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
            .padding(
                horizontal = 5.dp,
                vertical = 5.dp)
        ) {
            Text(
                text = userName,
                fontSize = 12.sp,
                fontFamily = Ledger,
            )
            Text(
                text = createdAt,
                color = Gray,
                fontSize = 8.sp,
                modifier = Modifier.padding(3.dp)
            )
        }
    }

}