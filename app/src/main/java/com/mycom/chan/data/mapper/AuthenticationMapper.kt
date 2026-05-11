package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.response.AuthResponse
import com.mycom.chan.domain.model.Authentication

fun AuthResponse.toAuthResult(): Authentication {
    return Authentication(
        accessToken = this.token,
        refreshToken = this.refreshToken,
        user = this.user.toUser()
    )
}