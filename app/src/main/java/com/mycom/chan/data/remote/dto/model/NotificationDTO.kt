package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class NotificationDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("sender")
    val sender: InfoDTO?,
    @SerializedName("type")
    val type: String,
    @SerializedName("targetId")
    val targetId: String?,
    @SerializedName("message")
    val message: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("read")
    val read: Boolean
)