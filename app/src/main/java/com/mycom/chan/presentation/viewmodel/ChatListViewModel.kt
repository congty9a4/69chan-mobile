package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Conversation
import com.mycom.chan.domain.repository.ChatRepository
import com.mycom.chan.presentation.uimodel.ChatFilter
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel
import com.mycom.chan.presentation.util.formatRelativeTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatListState(
    val conversations: List<Conversation> = emptyList(),
    val filter: ChatFilter = ChatFilter.ALL,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListState())
    val uiState: StateFlow<ChatListState> = _uiState.asStateFlow()

    // Map conversations → UI items, currentUserId để xác định người nhận
    fun chatItems(currentUserId: String): List<ChatListItemUiModel> {
        val list = if (_uiState.value.filter == ChatFilter.UNREAD)
            _uiState.value.conversations.filter { it.unreadCount > 0 }
        else
            _uiState.value.conversations

        return list.map { conv ->
            val otherUserId = conv.participantIds.find { it != currentUserId } ?: "Unknown"
            ChatListItemUiModel(
                conversationId = conv.id,
                senderUsername = otherUserId,       // API chưa trả username — dùng ID tạm
                senderAvatar = null,
                lastMessage = conv.lastMessage ?: "",
                lastMessageTimestamp = formatRelativeTime(conv.lastMessageTime),
                unreadMessageCount = conv.unreadCount
            )
        }
    }

    init { loadConversations() }

    fun loadConversations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = chatRepository.getConversations()) {
                is Result.Success -> _uiState.update {
                    it.copy(conversations = result.data, isLoading = false)
                }
                is Result.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun setFilter(filter: ChatFilter) = _uiState.update { it.copy(filter = filter) }

}