package com.mycom.chan.presentation.page

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.presentation.components.login_register.AuthScreenContainer
import com.mycom.chan.presentation.components.login_register.RegisterButton
import com.mycom.chan.presentation.components.login_register.RegisterInput
import com.mycom.chan.presentation.viewmodel.RegisterEvent
import com.mycom.chan.presentation.viewmodel.RegisterViewModel

@Composable
fun Register(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: (email: String) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess -> onRegisterSuccess(event.email)
                is RegisterEvent.ShowError -> snackbarHostState.showSnackbar(message = event.message)
            }
        }
    }

    AuthScreenContainer(snackbarHostState = snackbarHostState, enterFromStart = true) {
        RegisterInput(
            username = uiState.username,
            email = uiState.email,
            password = uiState.password,
            confirmPassword = uiState.confirmPassword,
            onUsernameChange = { viewModel.onUsernameChange(it) },
            onEmailChange = { viewModel.onEmailChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            onConfirmPasswordChange = { viewModel.onConfirmPasswordChange(it) },
            onSubmit = { if (!uiState.isLoading) viewModel.register() }
        )
        RegisterButton(
            isLoading = uiState.isLoading,
            onLoginClick = onNavigateToLogin,
            onRegisterClick = { viewModel.register() }
        )
    }
}
