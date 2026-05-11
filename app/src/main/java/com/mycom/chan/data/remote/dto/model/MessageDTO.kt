package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class MessageDTO (
    @SerializedName("id")
    val messageId: Long,
    @SerializedName("sender_id")
    val senderId: String,
    @SerializedName("receiver_id")
    val receiverId: String,
    @SerializedName("content")
    val messageContent: String,
    @SerializedName("created_at")
    val createdAt: String
)