package com.mycom.chan.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("keyName") val keyName: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("coverPhotoUrl") val coverPhotoUrl: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("birthday") val birthday: String? = null,
    @SerializedName("phone") val phone: String? = null
)
