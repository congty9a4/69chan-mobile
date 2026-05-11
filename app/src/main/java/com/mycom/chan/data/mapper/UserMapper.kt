package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.UserDTO
import com.mycom.chan.domain.model.User

fun UserDTO.toUser(): User {
    return User(
        id = this.userId,
        userName = this.username,
        fullName = this.fullname ?: "",
        avatarUrl = this.avatar ?: "",
    )
}