package com.mycom.chan.domain.model

data class Profile(
    val id: Int,
    val bio: String?,
    val location: String?,
    val birthday: String?,
    val phone: String?,
    val user: User,
    val avatarUrl: String?,
    val coverPhotoUrl: String?,
    val createdAt: String,
    val updatedAt: String
)