package com.mycom.chan.domain.model

data class Authentication (
    val accessToken: String,
    val refreshToken: String,
    val user: User
)