package com.mycom.chan.presentation.page

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.presentation.components.login_register.AuthScreenContainer
import com.mycom.chan.presentation.components.login_register.LoginButton
import com.mycom.chan.presentation.components.login_register.LoginInput
import com.mycom.chan.presentation.viewmodel.LoginEvent
import com.mycom.chan.presentation.viewmodel.LoginViewModel

@Composable
fun Login(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToVerifyEmail: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is LoginEvent.loginSuccess -> {
                    snackbarHostState.showSnackbar(message = "Logged in")
                    onLoginSuccess()
                }
                is LoginEvent.ShowError -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    AuthScreenContainer(snackbarHostState = snackbarHostState) {
        LoginInput(
            email = uiState.email,
            password = uiState.password,
            rememberMe = uiState.rememberMe,
            onEmailChange = { viewModel.onEmailChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            onRememberMeChange = { viewModel.onRememberMeChange(it) },
            onForgotPasswordClick = onNavigateToForgotPassword,
            onSubmit = { if (!uiState.isLoading) viewModel.login() }
        )
        LoginButton(
            isLoading = uiState.isLoading,
            onLoginClick = { viewModel.login() },
            onGoogleClick = { /* TODO: Implement Google login */ },
            onFacebookClick = { /* TODO: Implement Facebook login */ },
            onSignUpClick = onNavigateToRegister
        )
        // Needs the email to know where the code was sent, so it stays disabled until one is typed
        TextButton(
            onClick = { onNavigateToVerifyEmail(uiState.email.trim()) },
            enabled = uiState.email.isNotBlank()
        ) {
            Text("Haven't verified your email?")
        }
    }
}
