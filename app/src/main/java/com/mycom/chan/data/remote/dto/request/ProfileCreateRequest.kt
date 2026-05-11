package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ProfileCreateRequest(
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("birthday")
    val birthday: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("fullname")
    val fullname: String? = null
)