package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Post
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

data class FeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nextCursor: String? = null,
    val hasNext: Boolean = false
)

sealed interface FeedEvent {
    data class ShowError(val message: String) : FeedEvent
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<FeedEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    init { loadFeed() }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = postsRepository.getFeed()) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        posts = result.data.posts,
                        nextCursor = result.data.nextCursor,
                        hasNext = result.data.hasNext,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(FeedEvent.ShowError(result.message ?: "Load failed"))
                }
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (!state.hasNext || state.isLoadingMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            when (val result = postsRepository.getFeed(after = state.nextCursor)) {
                is Result.Success -> _uiState.update { s ->
                    s.copy(
                        posts = s.posts + result.data.posts,
                        nextCursor = result.data.nextCursor,
                        hasNext = result.data.hasNext,
                        isLoadingMore = false
                    )
                }
                is Result.Error -> _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    fun toggleLike(postId: String) {
        _uiState.update { state ->
            state.copy(posts = state.posts.map { post ->
                if (post.id == postId) post.copy(
                    isLiked = !post.isLiked,
                    likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                ) else post
            })
        }
        viewModelScope.launch {
            if (postsRepository.likePost(postId) is Result.Error) {
                _uiState.update { state ->
                    state.copy(posts = state.posts.map { post ->
                        if (post.id == postId) post.copy(
                            isLiked = !post.isLiked,
                            likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                        ) else post
                    })
                }
            }
        }
    }
}
