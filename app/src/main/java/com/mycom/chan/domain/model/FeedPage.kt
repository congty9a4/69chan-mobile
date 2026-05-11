package com.mycom.chan.domain.model

data class FeedPage(
    val posts: List<Post>,
    val nextCursor: String?,
    val hasNext: Boolean
)