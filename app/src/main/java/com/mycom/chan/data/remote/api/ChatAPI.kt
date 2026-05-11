package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.model.ConversationDTO
import com.mycom.chan.data.remote.dto.model.MessageDTO
import com.mycom.chan.data.remote.dto.response.BaseResponse
import com.mycom.chan.data.remote.dto.response.MessageHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Query

interface ChatAPI {

    @GET("conversations/me")
    suspend fun getMyConversations(): Response<BaseResponse<List<ConversationDTO>>>

    @GET("conversations/{id}")
    suspend fun getConversation(
        @Path("id") conversationId: String
    ): Response<BaseResponse<ConversationDTO>>

    @GET("conversations/{id}/history")
    suspend fun getMessageHistory(
        @Path("id") conversationId: String,
        @Query("limit") limit: Int = 20,
        @Query("cursor") cursor: Long? = null
    ): Response<MessageHistoryResponse>
}

