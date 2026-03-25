package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class MediaDTO (
    @SerializedName("id")
    val mediaID: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("uploaded_at")
    val uploadAt: String
)
