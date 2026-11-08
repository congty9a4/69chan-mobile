package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.uimodel.MessageUiModel

@Composable
fun ChatContainer(
    messages: List<MessageUiModel>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Jump to the newest message whenever one arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(vertical = Spacing.md)
    ) {
        items(
            items = messages,
            key = { it.id }
        ) { message ->
            MessageBubble(message = message)
        }
    }
}
