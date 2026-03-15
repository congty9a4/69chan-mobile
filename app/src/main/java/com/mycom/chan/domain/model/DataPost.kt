package com.mycom.chan.domain.model

import com.mycom.chan.data.remote.dto.model.UserDTO

data class Post (
    val id: String,
    val tags: List<String>,
    val caption: String,
    val medias: List<Media>,
    val user: UserDTO,
    val scope: String,
    val likes: Int,
    val comment: Int,
    val isLiked: Boolean
)