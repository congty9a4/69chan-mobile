package com.mycom.chan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.mycom.chan.data.remote.dto.model.Result

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

sealed interface RegisterEvent  {
    object RegisterSuccess : RegisterEvent
    data class ShowError(val message: String) : RegisterEvent
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<RegisterEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username=username) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    private fun sendEvent(event: RegisterEvent) {
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

    fun register() {
        val currentState = _uiState.value

        if(currentState.email.isBlank()) {
            sendEvent(RegisterEvent.ShowError("Email is required."))
        }

        if(!isValidEmail(currentState.email)) {
            sendEvent(RegisterEvent.ShowError("Email is invalid"))
        }

        if (currentState.password.isBlank()) {
            sendEvent(RegisterEvent.ShowError("Password is required."))
        }

        if (currentState.password.length < 6) {
            sendEvent(RegisterEvent.ShowError("Password must be at least 6 characters"))
            return
        }

        performRegister { authRepository.register(currentState.username, currentState.email, currentState.password) }
    }
    private fun performRegister(registerAction: suspend () -> Result<Boolean>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = registerAction()) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEvent(RegisterEvent.RegisterSuccess)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEvent(RegisterEvent.ShowError(result.message ?: "Register failed"))
                }
            }
        }
    }
}