package com.mycom.chan.presentation.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.eventFlow.collect { event ->
            when (event) {
                is LoginEvent.loginSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "Login Successfully"
                    )
                    onLoginSuccess()
                }
                is LoginEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFc3fbe8),
                        Color(0xFFe8c3fb),
                        Color(0xFFfbe8c3)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(400)) +
                    slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                    scaleIn(initialScale = 0.9f, animationSpec = tween(400)),
            exit = fadeOut(animationSpec = tween(400)) +
                    slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                    scaleOut(targetScale = 1.1f, animationSpec = tween(400))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .widthIn(max = 400.dp)
                    .heightIn(min = 500.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.4f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginInput(
                        email = uiState.email,
                        password = uiState.password,
                        rememberMe = uiState.rememberMe,
                        onEmailChange = { viewModel.onEmailChange(it) },
                        onPasswordChange = { viewModel.onPasswordChange(it) },
                        onRememberMeChange = { viewModel.onRememberMeChange(it) },
                        onForgotPasswordClick = onNavigateToForgotPassword
                    )
                    LoginButton(
                        isLoading = uiState.isLoading,
                        onLoginClick = { viewModel.login() },
                        onGoogleClick = { /* TODO: Implement Google login */ },
                        onFacebookClick = { /* TODO: Implement Facebook login */ },
                        onSignUpClick = onNavigateToRegister
                    )
                    TextButton(onClick = { onNavigateToVerifyEmail(uiState.email) }) {
                        Text("Haven't verify your account?",
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                    // Snackbar for showing errors
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        Snackbar(
                            snackbarData = data
                        )
                    }
                }
            }
        }
        }
    }
}
