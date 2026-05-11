package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel
import com.mycom.chan.presentation.uimodel.toShape

@Composable
fun MessageBubble(message: MessageUiModel) {
    // Giữ nguyên logic sắp xếp trái/phải của bạn
    val horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 8.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        // Lấy chiều rộng màn hình hiện tại
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        // Giới hạn chiều rộng tối đa của bubble là 80% chiều rộng màn hình
        val maxWidth = screenWidth * 0.6f

        // Lấy hình dạng từ logic đã có
        val shape = message.position.toShape(isMine = message.isMine)

        Surface(
            // ÁP DỤNG MODIFIER widthIn Ở ĐÂY
            modifier = Modifier.widthIn(max = maxWidth),
            shape = shape,
            color = if (message.isMine) Color(0xFF5865F2) else Color(0xFF4A4E57)
        ) {
            Text(
                text = message.content,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF2C2F33)
@Composable
fun MessageBubblePreview() {
    Column {
        MessageBubble(
            MessageUiModel(1, "Đây là một tin nhắn ngắn.", true, MessagePosition.SINGLE)
        )
        MessageBubble(
            MessageUiModel(2, "Còn đây là một tin nhắn rất rất dài để kiểm tra xem nó có tự động xuống dòng và giới hạn chiều rộng một cách chính xác hay không.", false, MessagePosition.SINGLE)
        )
        MessageBubble(
            MessageUiModel(3, "Ok", false, MessagePosition.BOTTOM)
        )
    }
}
