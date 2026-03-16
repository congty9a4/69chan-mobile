package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class UserDTO (
    @SerializedName("id")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("avatar")
    val avatar: String
)