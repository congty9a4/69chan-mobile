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
data class LoginState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface LoginEvent  {
    object loginSuccess : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<LoginEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onRememberMeChange(rememberMe: Boolean) {
        _uiState.update { it.copy(rememberMe=rememberMe) }
    }

    private fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

    fun login() {
        val currentState = _uiState.value

        if(currentState.email.isBlank()) {
            sendEvent(LoginEvent.ShowError("Email is required."))
            return
        }

        if(!isValidEmail(currentState.email)) {
            sendEvent(LoginEvent.ShowError("Email is invalid"))
            return
        }

        if (currentState.password.isBlank()) {
            sendEvent(LoginEvent.ShowError("Password is required."))
            return
        }

        if (currentState.password.length < 6) {
            sendEvent(LoginEvent.ShowError("Password must be at least 6 characters"))
            return
        }

        performLogin { authRepository.login(currentState.email, currentState.password) }
    }
    private fun performLogin(loginAction: suspend () -> Result<Boolean>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = loginAction()) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEvent(LoginEvent.loginSuccess)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendEvent(LoginEvent.ShowError(result.message ?: "Login failed"))
                }
            }
        }
    }

    fun loginAsGuest() {
        performLogin { authRepository.guestLogin() }
    }
}