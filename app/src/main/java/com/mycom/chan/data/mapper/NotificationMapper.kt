package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.NotificationDTO
import com.mycom.chan.domain.model.Notification


fun NotificationDTO.toNotification() = Notification(
    id = id,
    sender = sender?.toInfo(),
    type = type,
    targetId = targetId,
    message = message,
    createdAt = createdAt,
    read = read
)