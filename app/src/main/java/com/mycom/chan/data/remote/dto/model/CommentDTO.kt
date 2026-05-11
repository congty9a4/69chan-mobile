package com.mycom.chan.data.remote.dto.model

import com.google.gson.annotations.SerializedName

data class CommentDTO (
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("post_id")
    val postId: String,
    @SerializedName("user")
    val user: UserDTO,
    @SerializedName("children")
    val children: List<CommentDTO> = emptyList(),
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)