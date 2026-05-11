package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.mycom.chan.data.remote.dto.model.ContentDTO

data class FeedPageInfo(
    @SerializedName("next_cursor")
    val nextCursor: String?,
    @SerializedName("has_next")
    val hasNext: Boolean
)

data class FeedResponse(
    @SerializedName("data")
    val data: List<ContentDTO>,
    @SerializedName("page_info")
    val pageInfo: FeedPageInfo
)
