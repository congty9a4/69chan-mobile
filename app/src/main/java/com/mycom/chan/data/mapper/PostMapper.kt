package com.mycom.chan.data.mapper

data class PostMapper (
    val page: Int = 1,
    val size: Int = 10,
    val sortBy: String = "created_at",
    val sortDir: String = "desc"
)

fun PostMapper.toMap(): Map<String, String> {
    return mapOf(
        "page" to page.toString(),
        "size" to size.toString(),
        "sortBy" to sortBy,
        "sortDir" to sortDir
    )
}