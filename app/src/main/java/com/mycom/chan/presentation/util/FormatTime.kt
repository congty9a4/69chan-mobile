package com.mycom.chan.presentation.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

/**
 * Accepts what the backend sends: "2026-11-08T16:40:39.466+07:00" (with offset),
 * "2026-11-08T09:40:39Z", or "2026-11-08T16:40:39" (no offset: read as local time).
 */
private fun parseInstant(dateTime: String): Instant =
    runCatching { OffsetDateTime.parse(dateTime).toInstant() }
        .recoverCatching { Instant.parse(dateTime) }
        .getOrElse { LocalDateTime.parse(dateTime).atZone(ZoneId.systemDefault()).toInstant() }

fun formatRelativeTime(dateTime: String): String {
    return try {
        formatRelativeTime(parseInstant(dateTime).toEpochMilli())
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