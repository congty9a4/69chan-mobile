package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("page") val page: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("totalItems") val totalItems: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)
