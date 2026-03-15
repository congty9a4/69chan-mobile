package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("success")
    val isSuccess: Boolean,
    @SerializedName("status")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("timestamp")
    val timestamp: String?
)