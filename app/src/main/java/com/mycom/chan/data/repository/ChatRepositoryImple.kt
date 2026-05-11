package com.mycom.chan.data.repository

import android.util.Log
import com.mycom.chan.data.mapper.toConversation
import com.mycom.chan.data.mapper.toResult
import com.mycom.chan.data.remote.api.ChatAPI
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.websocket.StompClient
import com.mycom.chan.data.remote.websocket.WsIncomingMessage
import com.mycom.chan.domain.model.Conversation
import com.mycom.chan.domain.model.MessageHistory
import com.mycom.chan.domain.repository.ChatRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ChatRepositoryImple @Inject constructor(
    private val chatAPI: ChatAPI,
    private val stompClient: StompClient
) : BaseRepository(), ChatRepository {

    override suspend fun getConversations(): Result<List<Conversation>> =
        handleResponse(
            apiCall = { chatAPI.getMyConversations() },
            transform = { list -> list.map { it.toConversation() } }
        )

    override suspend fun getConversation(conversationId: String): Result<Conversation> =
        handleResponse(
            apiCall = { chatAPI.getConversation(conversationId) },
            transform = { it.toConversation() }
        )

    override suspend fun getMessageHistory(
        conversationId: String,
        limit: Int,
        cursor: Long?
    ): Result<MessageHistory> {
        return try {
            val response = chatAPI.getMessageHistory(conversationId, limit, cursor)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.toResult())
            } else {
                Result.Error(
                    exception = Exception("HTTP ${response.code()}"),
                    message = "Không tải được lịch sử tin nhắn"
                )
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "getMessageHistory failed", e)
            Result.Error(e, "Lỗi kết nối")
        }
    }

    override suspend fun connectWebSocket() = stompClient.connect()
    override fun disconnectWebSocket() = stompClient.disconnect()
    override fun observeIncomingMessages(): SharedFlow<WsIncomingMessage> = stompClient.messages
    override fun sendMessage(receiverId: String, content: String) =
        stompClient.sendMessage(receiverId, content)
}
