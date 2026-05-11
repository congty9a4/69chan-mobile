package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class ConversationDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("participant_ids")
    val participantIds: List<String>,
    @SerializedName("last_message")
    val lastMessage: String?,
    @SerializedName("last_sender_id")
    val lastSenderId: String?,
    @SerializedName("last_message_time")
    val lastMessageTime: Long,
    @SerializedName("unread_count")
    val unreadCount: Int
)