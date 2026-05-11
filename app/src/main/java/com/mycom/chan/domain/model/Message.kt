package com.mycom.chan.domain.model


data class Message(
    val id: Long,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: String,
    val isSending: Boolean = false
)
