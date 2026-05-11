package com.mycom.chan.presentation.uimodel

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Enum để xác định vị trí của một tin nhắn trong một chuỗi liên tiếp
 * từ cùng một người gửi.
 */
enum class MessagePosition { 
    SINGLE,
    TOP,    
    MIDDLE, 
    BOTTOM  
}

@Composable
fun MessagePosition.toShape(isMine: Boolean): Shape {
    return remember(this, isMine) {
        when (this) {
            MessagePosition.TOP -> {
                if (isMine) {
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp,
                        bottomStart = 16.dp, bottomEnd = 4.dp
                    )
                } else {
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp,
                        bottomStart = 4.dp, bottomEnd = 16.dp
                    )
                }
            }
            MessagePosition.MIDDLE -> {
                if (isMine) {
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 4.dp,
                        bottomStart = 16.dp, bottomEnd = 4.dp
                    )
                } else {
                    RoundedCornerShape(
                        topStart = 4.dp, topEnd = 16.dp,
                        bottomStart = 4.dp, bottomEnd = 16.dp
                    )
                }
            }
            MessagePosition.BOTTOM -> {
                if (isMine) {
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 4.dp,
                        bottomStart = 16.dp, bottomEnd = 16.dp
                    )
                } else {
                    RoundedCornerShape(
                        topStart = 4.dp, topEnd = 16.dp,
                        bottomStart = 16.dp, bottomEnd = 16.dp
                    )
                }
            }
            // MessagePosition.SINGLE
            else -> RoundedCornerShape(16.dp)
        }
    }
}
