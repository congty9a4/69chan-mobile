package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ProfileCreationRequest(
    @SerializedName("fullname") val fullname: String,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("country") val country: String? = null
)
