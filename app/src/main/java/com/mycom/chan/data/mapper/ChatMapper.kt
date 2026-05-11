package com.mycom.chan.data.mapper

import com.mycom.chan.data.remote.dto.model.ConversationDTO
import com.mycom.chan.data.remote.dto.model.MessageDTO
import com.mycom.chan.data.remote.dto.response.MessageHistoryResponse
import com.mycom.chan.domain.model.Conversation
import com.mycom.chan.domain.model.Message
import com.mycom.chan.domain.model.MessageHistory

fun ConversationDTO.toConversation() = Conversation(
    id = id,
    participantIds = participantIds,
    lastMessage = lastMessage,
    lastSenderId = lastSenderId,
    lastMessageTime = lastMessageTime,
    unreadCount = unreadCount
)

fun MessageDTO.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    receiverId = receiverId,
    content = messageContent,
    createdAt = createdAt
)

fun MessageHistoryResponse.toResult() = MessageHistory(
    messages = data.map { it.toMessage() },
    nextCursor = pageInfo.nextCursor,
    hasNext = pageInfo.hasNext
)