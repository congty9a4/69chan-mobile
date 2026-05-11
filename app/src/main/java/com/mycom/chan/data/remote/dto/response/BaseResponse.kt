package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import okhttp3.internal.http2.ErrorCode
import java.time.Instant

data class BaseResponse<T> (
    @SerializedName("body")
    val body: T?,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: String?,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val errorCode: Int? = null
)