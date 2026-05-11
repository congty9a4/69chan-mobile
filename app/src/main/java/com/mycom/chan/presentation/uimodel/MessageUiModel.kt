package com.mycom.chan.presentation.uimodel

import com.mycom.chan.presentation.uimodel.MessagePosition

data class MessageUiModel(
    val id: Long,
    val content: String,
    val isMine: Boolean,
    val position: MessagePosition,
    val createdAt: String = "",
    val isSending: Boolean = false
)
