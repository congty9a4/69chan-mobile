package com.mycom.chan.domain.repository

import com.mycom.chan.domain.model.Message
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.websocket.WsIncomingMessage
import com.mycom.chan.domain.model.Conversation
import com.mycom.chan.domain.model.MessageHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChatRepository {

    suspend fun getConversations(): Result<List<Conversation>>
    suspend fun getConversation(conversationId: String): Result<Conversation>
    suspend fun getMessageHistory(
        conversationId: String,
        limit: Int = 20,
        cursor: Long? = null
    ): Result<MessageHistory>
    suspend fun connectWebSocket()
    fun disconnectWebSocket()
    fun observeIncomingMessages(): SharedFlow<WsIncomingMessage>
    fun sendMessage(receiverId: String, content: String)
}
