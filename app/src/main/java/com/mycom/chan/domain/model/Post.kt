package com.mycom.chan.domain.model

data class Post (
    val id: String,
    val tags: List<String>,
    val caption: String,
    val medias: List<Media>,
    val user: User,
    val scope: String,
    val likes: Int,
    val comment: Int,
    val isLiked: Boolean
)