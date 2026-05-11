package com.mycom.chan.domain.model

data class Conversation(
    val id: String,
    val participantIds: List<String>,
    val lastMessage: String?,
    val lastSenderId: String?,
    val lastMessageTime: Long,
    val unreadCount: Int
)