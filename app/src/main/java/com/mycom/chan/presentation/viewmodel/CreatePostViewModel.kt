package com.mycom.chan.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
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

data class CreatePostState(
    val caption: String = "",
    val tagsInput: String = "",
    val isPublic: Boolean = true,
    val isLoading: Boolean = false,
    val selectedUris: List<Uri> = emptyList()
)

sealed interface CreatePostEvent {
    object CreateSuccess : CreatePostEvent
    data class ShowError(val message: String) : CreatePostEvent
}

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostState())
    val uiState: StateFlow<CreatePostState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<CreatePostEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onCaptionChange(value: String) = _uiState.update { it.copy(caption = value) }
    fun onTagsChange(value: String) = _uiState.update { it.copy(tagsInput = value) }
    fun onIsPublicChange(value: Boolean) = _uiState.update { it.copy(isPublic = value) }

    fun addUri(uri: Uri) = _uiState.update { it.copy(selectedUris = it.selectedUris + uri) }
    fun removeUri(uri: Uri) = _uiState.update { it.copy(selectedUris = it.selectedUris - uri) }
    fun createPost() {
        val caption = _uiState.value.caption.trim()
        if (caption.isBlank()) {
            viewModelScope.launch { _eventChannel.send(CreatePostEvent.ShowError("Caption không được để trống")) }
            return
        }
        val tags = _uiState.value.tagsInput.split(",").map { it.trim() }.filter { it.isNotBlank() }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = postsRepository.createPost(caption, tags, _uiState.value.isPublic, _uiState.value.selectedUris)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(CreatePostEvent.CreateSuccess)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(CreatePostEvent.ShowError(result.message ?: "Đăng bài thất bại"))
                }
            }
        }
    }
}