package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("text") val text: String
)
