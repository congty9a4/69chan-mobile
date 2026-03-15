package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class UserDTO (
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fullname")
    val fullname: String?=null,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatar")
    val avatar: String?=null,
    @SerializedName("createdAt")
    val createdAt: Instant,
    @SerializedName("updatedAt")
    val updatedAt: Instant
)