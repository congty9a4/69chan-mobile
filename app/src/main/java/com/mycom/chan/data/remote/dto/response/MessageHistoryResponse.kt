package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import com.mycom.chan.data.remote.dto.model.MessageDTO

data class PageInfo(
    @SerializedName("next_cursor") val nextCursor: Long?,
    @SerializedName("has_next") val hasNext: Boolean
)

data class MessageHistoryResponse(
    @SerializedName("data") val data: List<MessageDTO>,
    @SerializedName("page_info") val pageInfo: PageInfo
)