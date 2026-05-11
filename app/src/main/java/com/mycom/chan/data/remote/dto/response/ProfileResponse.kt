package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.mycom.chan.data.remote.dto.model.UserDTO

data class ProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val user: UserDTO,
    @SerializedName("bio") val bio: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("cover_photo_url") val coverPhotoUrl: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
