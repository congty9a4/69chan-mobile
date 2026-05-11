package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel

@Composable
fun ChatListItem(
    item: ChatListItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(border = BorderStroke(width=3.dp, color = Color.White))
            .clickable(onClick = onClick),
        color = Color(0xFF4a4E57)
    ) {
        Row (
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Avatar(
                modifier = Modifier.size(50.dp),
                imageUrl = item.senderAvatar,
                isEditable = false
            )
            VerticalDivider(
                modifier = Modifier
                    .height(35.dp)
                    .width(5.dp),
                color = Color.Gray.copy(alpha = 0.5f)
            )
            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Text(
                    text = item.senderUsername,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Row (
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.lastMessage,
                        fontSize = 14.sp,
                        color = Color.White.copy(0.7F),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text (
                        text = item.lastMessageTimestamp,
                        fontSize = 12.sp,
                        color = Color.White.copy(0.2F)
                    )
                }

            }

        }
    }

}
