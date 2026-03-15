package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LogoutRequest (
    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("accessToken")
    val accessToken: String
)