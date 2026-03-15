package com.mycom.chan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.model.UserDTO
import com.mycom.chan.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoggedIn: Boolean = false,
    val currentUser: UserDTO? = null,
    val isLoading: Boolean = true,
)

sealed interface AuthEvent{
    data class ShowError(val message: String) : AuthEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<AuthEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private fun observerAuthFlow( ) {
        viewModelScope.launch {
            authRepository.observeLoginState().collect { token->
                val isLoggedIn = !token.isNullOrBlank()
                val currentUser = if (isLoggedIn) {
                    authRepository.getCurrentUser()
                } else null

                _uiState.update {
                    it.copy(
                        isLoggedIn = isLoggedIn,
                        currentUser = currentUser,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = authRepository.logout()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            currentUser = null,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = true) }
                    _eventChannel.send(AuthEvent.ShowError(result.message ?: "Logout failed"))
                }
            }
        }
    }

    fun refreshUserData() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            _uiState.update { it.copy(currentUser = currentUser) }
        }
    }
}
