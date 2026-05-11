package com.mycom.chan.presentation.uimodel

/**
 * Represents the data needed to display a single item in the chat list.
 * This model is prepared by the ViewModel and consumed by the UI.
 */
data class ChatListItemUiModel(
    val conversationId: String,
    val senderUsername: String,
    val senderAvatar: String?,
    val lastMessage: String,
    val lastMessageTimestamp: String,
    val unreadMessageCount: Int
)
