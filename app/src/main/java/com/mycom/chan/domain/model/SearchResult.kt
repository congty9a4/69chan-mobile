package com.mycom.chan.domain.model

data class SearchResult(
    val users: List<UserSearchResult> = emptyList(),
    val posts: List<Post> = emptyList()
)
