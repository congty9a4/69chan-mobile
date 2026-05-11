package com.mycom.chan.domain.repository

import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Notification

interface NotificationRepository {
    suspend fun getMyNotifications(): Result<List<Notification>>
    suspend fun getUnreadCount(): Result<Long>
    suspend fun markAsRead(notificationId: String): Result<String>
}