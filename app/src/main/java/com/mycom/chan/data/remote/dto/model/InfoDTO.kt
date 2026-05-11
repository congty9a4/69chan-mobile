package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class InfoDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String?
)
