package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.ContentDTO
import com.mycom.chan.domain.model.Post

fun ContentDTO.toPost(): Post {
    return Post(
        id = this.contentID,
        tags = this.tags,
        caption = this.caption,
        medias = this.media.map { it.toMedia() },
        user = this.userDTO,
        scope = this.scope,
        likes = this.likes,
        comment = this.comments,
        isLiked = this.userLiked
    )
}

fun List<ContentDTO>.toPosts(): List<Post> {
    return this.map { it.toPost() }
}
