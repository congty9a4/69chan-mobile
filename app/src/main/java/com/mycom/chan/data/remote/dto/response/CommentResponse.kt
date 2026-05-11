package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.mycom.chan.data.remote.dto.model.UserDTO

data class CommentResponse(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("post_id") val postId: String,
    @SerializedName("user") val user: UserDTO,
    @SerializedName("children") val children: List<CommentResponse>?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)
