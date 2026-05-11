package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
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

data class VerifyEmailState(
    val otp: String = "",
    val isLoading: Boolean = false
)

sealed interface VerifyEmailEvent {
    object VerifySuccess : VerifyEmailEvent
    object ResendSuccess : VerifyEmailEvent
    data class ShowError(val message: String) : VerifyEmailEvent
}

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyEmailState())
    val uiState: StateFlow<VerifyEmailState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<VerifyEmailEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun onOtpChange(otp: String) {
        _uiState.update { it.copy(otp = otp) }
    }

    fun verify(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.verifyEmail(email, _uiState.value.otp)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(VerifyEmailEvent.VerifySuccess)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(VerifyEmailEvent.ShowError(result.message ?: "Verification failed"))
                }
            }
        }
    }

    fun resendOtp(email: String) {
        viewModelScope.launch {
            when (authRepository.resendOtp(email)) {
                is Result.Success -> _eventChannel.send(VerifyEmailEvent.ResendSuccess)
                is Result.Error -> _eventChannel.send(VerifyEmailEvent.ShowError("Resend OTP failed"))
            }
        }
    }
}
