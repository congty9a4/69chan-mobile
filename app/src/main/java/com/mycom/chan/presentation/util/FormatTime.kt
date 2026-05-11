package com.mycom.chan.presentation.util

fun formatRelativeTime(dateTime: String): String {
    return try {
        val parsed = java.time.LocalDateTime.parse(dateTime)
        val now = java.time.LocalDateTime.now()
        val seconds = java.time.Duration.between(parsed, now).seconds
        when {
            seconds < 60        -> "Just now"
            seconds < 3600      -> "${seconds / 60}m ago"
            seconds < 86400     -> "${seconds / 3600}h ago"
            seconds < 86400 * 7 -> "${seconds / 86400}d ago"
            else                -> dateTime.substring(0, 10)
        }
    } catch (e: Exception) {
        dateTime
    }
}
fun formatRelativeTime(epochMs: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - epochMs
    return when {
        diff < 60_000L         -> "Just now"
        diff < 3_600_000L      -> "${diff / 60_000}m ago"
        diff < 86_400_000L     -> "${diff / 3_600_000}h ago"
        diff < 7 * 86_400_000L -> "${diff / 86_400_000}d ago"
        else -> java.text.SimpleDateFormat("dd/MM", java.util.Locale.getDefault())
            .format(java.util.Date(epochMs))
    }
}