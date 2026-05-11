package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.model.NotificationDTO
import com.mycom.chan.data.remote.dto.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationAPI {

    @GET("notifications")
    suspend fun getMyNotifications(): Response<BaseResponse<List<NotificationDTO>>>

    @GET("notifications/unread-count")
    suspend fun getUnreadCount(): Response<BaseResponse<Long>>

    @PATCH("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") notificationId: String): Response<BaseResponse<String>>
}