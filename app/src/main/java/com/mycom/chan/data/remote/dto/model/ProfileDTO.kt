package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class ProfileDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("user")
    val user: UserDTO,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("cover_photo_url")
    val coverPhotoUrl: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)