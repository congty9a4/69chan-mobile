package com.mycom.chan.data.mapper

data class PostMapper(
    val limit: Int = 10,
    val after: String? = null
)

fun PostMapper.toMap(): Map<String, String> = buildMap {
    put("limit", limit.toString())
    after?.let { put("after", it) }
}