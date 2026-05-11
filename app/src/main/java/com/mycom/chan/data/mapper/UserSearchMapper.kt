package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.SearchResultDTO
import com.mycom.chan.data.remote.dto.model.UserSearchResultDTO
import com.mycom.chan.domain.model.SearchResult
import com.mycom.chan.domain.model.UserSearchResult

fun UserSearchResultDTO.toUserSearchResult() = UserSearchResult(
    id = this.id,
    userName = this.username,
    email = this.email
)

fun SearchResultDTO.toSearchResult() = SearchResult(
    users = this.users.map { it.toUserSearchResult() },
    posts = this.posts.map { it.toPost() }
)