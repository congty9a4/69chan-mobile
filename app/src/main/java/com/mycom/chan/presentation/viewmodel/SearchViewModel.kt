package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Post
import com.mycom.chan.domain.model.UserSearchResult
import com.mycom.chan.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchFilter { ALL, POSTS, PEOPLE }

data class SearchState(
    val query: String = "",
    val filter: SearchFilter = SearchFilter.ALL,
    val posts: List<Post> = emptyList(),
    val users: List<UserSearchResult> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState
                .map { it.query }
                .distinctUntilChanged()
                .debounce(400)
                .filter { it.isNotBlank() }
                .collect { query -> performSearch(query) }
        }
    }

    fun onQueryChange(query: String) = _uiState.update { it.copy(query = query) }

    fun onFilterChange(filter: SearchFilter) {
        _uiState.update { it.copy(filter = filter) }
        if (_uiState.value.query.isNotBlank()) performSearch(_uiState.value.query)
    }

    private fun performSearch(query: String) {
        val filter = when (_uiState.value.filter) {
            SearchFilter.POSTS -> "posts"
            SearchFilter.PEOPLE -> "users"
            SearchFilter.ALL -> null
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = userRepository.search(query, filter)) {
                is Result.Success -> _uiState.update {
                    it.copy(isLoading = false, posts = result.data.posts, users = result.data.users)
                }
                is Result.Error -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}