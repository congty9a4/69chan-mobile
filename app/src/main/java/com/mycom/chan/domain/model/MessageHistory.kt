package com.mycom.chan.domain.model

data class MessageHistory(
    val messages: List<Message>,
    val nextCursor: Long?,
    val hasNext: Boolean
)