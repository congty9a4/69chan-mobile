package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.mycom.chan.data.remote.dto.model.ContentDTO
import com.mycom.chan.data.remote.dto.model.UserDTO

data class PostsResponse (
    @SerializedName("content")
    val content: List<ContentDTO>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("totalItems")
    val totalItems: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("prev")
    val prev: String
)