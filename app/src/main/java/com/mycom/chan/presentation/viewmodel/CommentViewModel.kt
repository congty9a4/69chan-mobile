package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Comment
import com.mycom.chan.domain.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentsState(
    val comments: List<Comment> = emptyList(),
    val isLoading: Boolean = false,
    val inputText: String = "",
    val isSending: Boolean = false,
    val replyingTo: Comment? = null
)

sealed interface CommentsEvent {
    data class ShowError(val message: String) : CommentsEvent
}

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentsState())
    val uiState: StateFlow<CommentsState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<CommentsEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private var currentPostId: String = ""

    fun init(postId: String) {
        if (currentPostId == postId) return
        currentPostId = postId
        loadComments()
    }

    fun loadComments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = postsRepository.getComments(currentPostId)) {
                is Result.Success -> _uiState.update {
                    it.copy(comments = result.data, isLoading = false)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(CommentsEvent.ShowError(result.message ?: "Failed to load comments"))
                }
            }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun setReplyTarget(comment: Comment) {
        _uiState.update { it.copy(replyingTo = comment) }
    }

    fun clearReplyTarget() {
        _uiState.update { it.copy(replyingTo = null) }
    }

    fun submitComment() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank() || _uiState.value.isSending) return

        val replyingTo = _uiState.value.replyingTo

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true) }

            val result = if (replyingTo != null) {
                postsRepository.addReply(currentPostId, replyingTo.id, text)
            } else {
                postsRepository.addComment(currentPostId, text)
            }

            when (result) {
                // Fix 4: reload thay vì append
                is Result.Success -> {
                    _uiState.update { it.copy(inputText = "", isSending = false, replyingTo = null) }
                    loadComments()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isSending = false) }
                    _eventChannel.send(CommentsEvent.ShowError(result.message ?: "Failed to post"))
                }
            }
        }
    }
}