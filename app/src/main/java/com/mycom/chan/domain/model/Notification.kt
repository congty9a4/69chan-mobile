package com.mycom.chan.domain.model

data class Notification(
    val id: String,
    val sender: Info?,
    val type: String,
    val targetId: String?,
    val message: String,
    val createdAt: String,
    val read: Boolean
)