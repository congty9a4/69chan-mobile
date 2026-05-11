package com.mycom.chan.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CursorPageResponse<T>(
    @SerializedName("data") val data: List<T>,
    @SerializedName("page_info") val pageInfo: PageInfo
) {
    data class PageInfo(
        @SerializedName("next_cursor") val nextCursor: String?,
        @SerializedName("has_next") val hasNext: Boolean
    )
}
