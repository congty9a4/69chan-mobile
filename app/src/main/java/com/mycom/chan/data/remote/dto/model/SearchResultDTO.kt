package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class SearchResultDTO(
    @SerializedName("users")
    val users: List<UserSearchResultDTO> = emptyList(),
    @SerializedName("posts")
    val posts: List<ContentDTO> = emptyList()
)