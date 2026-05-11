package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class PostRequest(
    @SerializedName("caption")
    val caption: String,
    @SerializedName("tags")
    val tags: List<String> = emptyList(),
    @SerializedName("is_public")
    val isPublic: Boolean = true
)