package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class RefreshRequest(
    @SerializedName("refresh_token")
    val refreshToken: String,
//    @SerializedName("access_token")
//    val accessToken: String
)
