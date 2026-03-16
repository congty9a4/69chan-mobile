package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("id")
    val userID: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)