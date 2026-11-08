package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel
import com.mycom.chan.presentation.uimodel.toShape

@Composable
fun MessageBubble(message: MessageUiModel) {
    val horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    // More space before the first message of a group, so groups from each sender read as blocks
    val topPadding = when (message.position) {
        MessagePosition.SINGLE, MessagePosition.TOP -> Spacing.sm
        else -> Spacing.xxs
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding, start = Spacing.md, end = Spacing.md),
        horizontalArrangement = horizontalArrangement
    ) {
        // Bubbles take at most 75% of the screen width, so long messages still show who sent them
        val maxWidth = LocalConfiguration.current.screenWidthDp.dp * 0.75f
        val shape = message.position.toShape(isMine = message.isMine)
        val colors = MaterialTheme.colorScheme

        Surface(
            modifier = Modifier.widthIn(max = maxWidth),
            shape = shape,
            color = if (message.isMine) colors.primary else colors.surfaceContainerHighest,
            contentColor = if (message.isMine) colors.onPrimary else colors.onSurface
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageBubblePreview() {
    ChanTheme {
        Column {
            MessageBubble(
                MessageUiModel(1, "Đây là một tin nhắn ngắn.", true, MessagePosition.SINGLE)
            )
            MessageBubble(
                MessageUiModel(2, "Còn đây là một tin nhắn rất rất dài để kiểm tra xem nó có tự động xuống dòng và giới hạn chiều rộng một cách chính xác hay không.", false, MessagePosition.TOP)
            )
            MessageBubble(
                MessageUiModel(3, "Ok", false, MessagePosition.BOTTOM)
            )
        }
    }
}
