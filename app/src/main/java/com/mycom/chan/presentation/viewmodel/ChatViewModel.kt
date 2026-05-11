package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.domain.repository.ChatRepository
import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.presentation.util.calculatePositions

data class ChatState(
    val messages: List<MessageUiModel> = emptyList(),
    val inputText: String = "",
    val isLoadingHistory: Boolean = false,
    val isSending: Boolean = false,
    val nextCursor: Long? = null,
    val hasMoreHistory: Boolean = false,
    val error: String? = null
)

sealed interface ChatEvent{
    object MessageSendSuccess: ChatEvent
    data class ShowError(val message: String) : ChatEvent
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatState())
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    private var conversationId: String = ""
    private var currentUserId: String = ""
    private var receiverId: String = ""

    fun init(conversationId: String, currentUserId: String, receiverId: String) {
        this.conversationId = conversationId
        this.currentUserId = currentUserId
        this.receiverId = receiverId
        loadHistory()
        connectAndObserve()
    }

    fun loadHistory(cursor: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingHistory = true) }
            when (val result = chatRepository.getMessageHistory(conversationId, cursor = cursor)) {
                is Result.Success -> {
                    val newMessages = result.data.messages.map { msg ->
                        MessageUiModel(
                            id = msg.id,
                            content = msg.content,
                            isMine = msg.senderId == currentUserId,
                            position = MessagePosition.SINGLE,
                            createdAt = msg.createdAt
                        )
                    }
                    // History trả về DESC → reverse để hiển thị đúng thứ tự
                    val merged = (newMessages.reversed() + _uiState.value.messages)
                        .distinctBy { it.id }
                        .calculatePositions()
                    _uiState.update {
                        it.copy(
                            messages = merged,
                            nextCursor = result.data.nextCursor,
                            hasMoreHistory = result.data.hasNext,
                            isLoadingHistory = false
                        )
                    }
                }
                is Result.Error -> _uiState.update {
                    it.copy(isLoadingHistory = false, error = result.message)
                }
            }
        }

    }

    fun loadMoreHistory() {
        val cursor = _uiState.value.nextCursor ?: return
        if (_uiState.value.hasMoreHistory) loadHistory(cursor)
    }

    private fun connectAndObserve() {
        viewModelScope.launch {
            chatRepository.connectWebSocket()
            chatRepository.observeIncomingMessages().collect { wsMsg ->
                // Chỉ xử lý tin nhắn thuộc conversation này
                if (wsMsg.senderId == receiverId || wsMsg.senderId == currentUserId) {
                    val newMsg = MessageUiModel(
                        id = wsMsg.id,
                        content = wsMsg.content,
                        isMine = wsMsg.senderId == currentUserId,
                        position = MessagePosition.SINGLE,
                        createdAt = wsMsg.createdAt
                    )
                    _uiState.update { state ->
                        val updated = (state.messages + newMsg).calculatePositions()
                        state.copy(messages = updated)
                    }
                }
            }
        }
    }
    fun onInputChange(text: String) = _uiState.update { it.copy(inputText = text) }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return
        val tempId = -System.currentTimeMillis()
        val optimistic = MessageUiModel(
            id = tempId,
            content = text,
            isMine = true,
            position = MessagePosition.SINGLE,
            isSending = true
        )
        _uiState.update { state ->
            val updated = (state.messages + optimistic).calculatePositions()
            state.copy(messages = updated, inputText = "")
        }

        chatRepository.sendMessage(receiverId, text)
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.disconnectWebSocket()
    }
}