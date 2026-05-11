package com.mycom.chan.data.remote.dto.model

import coil.request.Tags
import com.google.gson.annotations.SerializedName

data class ContentDTO (
    @SerializedName("id")
    val contentID: String,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("caption")
    val caption: String,
    @SerializedName("medias")
    val media: List<MediaDTO>,
    @SerializedName("user")
    val userDTO: UserDTO,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("user_liked")
    val userLiked: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val uploadedAt: String
)