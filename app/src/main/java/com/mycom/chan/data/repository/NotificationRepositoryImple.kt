package com.mycom.chan.data.repository

import com.mycom.chan.data.mapper.toNotification
import com.mycom.chan.data.remote.api.NotificationAPI
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Notification
import com.mycom.chan.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImple @Inject constructor(
    private val notificationAPI: NotificationAPI
) : NotificationRepository, BaseRepository() {

    override suspend fun getMyNotifications(): Result<List<Notification>> =
        handleResponse(
            apiCall = { notificationAPI.getMyNotifications() },
            transform = { list -> list.map { it.toNotification() } }
        )

    override suspend fun getUnreadCount(): Result<Long> =
        handleResponse(
            apiCall = { notificationAPI.getUnreadCount() },
            transform = { it }
        )

    override suspend fun markAsRead(notificationId: String): Result<String> =
        handleResponse(
            apiCall = { notificationAPI.markAsRead(notificationId) },
            transform = { it }
        )
}
