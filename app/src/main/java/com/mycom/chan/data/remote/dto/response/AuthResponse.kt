package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.mycom.chan.data.remote.dto.model.UserDTO

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: UserDTO,
    @SerializedName("refresh_token")
    val refreshToken: String
)