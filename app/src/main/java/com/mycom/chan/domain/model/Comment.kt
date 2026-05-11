package com.mycom.chan.domain.model

data class Comment(
    val id: String,
    val content: String,
    val user: User,
    val createdAt: String
)