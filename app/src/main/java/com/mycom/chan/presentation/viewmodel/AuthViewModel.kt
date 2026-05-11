package com.mycom.chan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.User
import com.mycom.chan.domain.repository.AuthRepository
import com.mycom.chan.domain.repository.ProfileRepository
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
    val currentUser: User? = null,
    val profileExists: Boolean? = null,
    val isLoading: Boolean = true,
)

sealed interface AuthEvent {
    data class ShowError(val message: String) : AuthEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<AuthEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        observeAuthFlow()
    }

    private fun observeAuthFlow() {
        viewModelScope.launch {
            authRepository.observeLoginState().collect { token ->
                Log.d("AuthViewModel", "Token received: ${if (token.isNullOrBlank()) "null/empty" else "present (${token.take(20)}...)"}")
                val isLoggedIn = !token.isNullOrBlank()
                val currentUser = if (isLoggedIn) authRepository.getCurrentUser() else null
                Log.d("AuthViewModel", "isLoggedIn=$isLoggedIn, currentUser=${currentUser?.id ?: "null"}")

                _uiState.update {
                    it.copy(
                        isLoggedIn = isLoggedIn,
                        currentUser = currentUser,
                        profileExists = null,
                        isLoading = currentUser != null
                    )
                }

                if (currentUser != null) {
                    checkProfileExists(currentUser.id)
                } else if (isLoggedIn) {
                    Log.w("AuthViewModel", "Token exists but getCurrentUser() returned null — user data not saved yet?")
                }
            }
        }
    }

    fun checkProfileExists(userId: String) {
        viewModelScope.launch {
            Log.d("AuthViewModel", "checkProfileExists($userId)")
            when (val result = profileRepository.profileExists(userId)) {
                is Result.Success -> {
                    Log.d("AuthViewModel", "profileExists result: ${result.data}")
                    _uiState.update {
                        it.copy(profileExists = result.data, isLoading = false)
                    }
                }
                is Result.Error -> {
                    Log.w("AuthViewModel", "profileExists error: ${result.message} | isLoggedIn=${_uiState.value.isLoggedIn}")
                    _uiState.update {
                        val stillLoggedIn = it.isLoggedIn
                        it.copy(profileExists = if (stillLoggedIn) false else null, isLoading = false)
                    }
                }
            }
        }
    }

    fun onProfileCreated() {
        _uiState.update { it.copy(profileExists = true) }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.logout()) {
                is Result.Success -> _uiState.update {
                    it.copy(isLoggedIn = false, currentUser = null, profileExists = null, isLoading = false)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
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
