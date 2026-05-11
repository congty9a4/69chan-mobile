package com.mycom.chan.presentation.components.chat

import android.graphics.LinearGradient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel

@Composable
fun ChatListContainer(
    modifier: Modifier,
    chatItems: List<ChatListItemUiModel>,
    onChatItemClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                    brush = Brush.linearGradient( listOf(
                        Color(0xff414650),
//                        Color(0xffd2d6d9),
                        Color(0xffd2d6d9)
                    )
                )
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = chatItems,
            key = { it.conversationId }
        ) { item ->
            ChatListItem(
                item = item,
                onClick = { onChatItemClick(item.conversationId) }
            )
        }
    }
}