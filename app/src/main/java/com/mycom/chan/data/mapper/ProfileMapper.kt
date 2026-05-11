package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.ProfileDTO
import com.mycom.chan.data.remote.dto.response.ProfileResponse
import com.mycom.chan.domain.model.Profile

fun ProfileDTO.toProfile(): Profile {
    return Profile(
        id = this.id,
        bio = this.bio,
        location = this.location,
        birthday = this.birthday,
        phone = this.phone,
        user = this.user.toUser(),
        avatarUrl = this.avatarUrl,
        coverPhotoUrl = this.coverPhotoUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ProfileResponse.toProfile(): Profile {
    return Profile(
        id = this.id,
        bio = this.bio,
        location = this.location,
        birthday = this.birthday,
        phone = this.phone,
        user = this.user.toUser(),
        avatarUrl = this.avatarUrl,
        coverPhotoUrl = this.coverPhotoUrl,
        createdAt = this.createdAt ?: "",
        updatedAt = this.updatedAt ?: ""
    )
}
