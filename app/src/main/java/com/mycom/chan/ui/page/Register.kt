package com.mycom.chan.ui.page

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.ui.components.login_register.RegisterButton
import com.mycom.chan.ui.components.login_register.RegisterInput
import com.mycom.chan.ui.viewmodel.LoginEvent
import com.mycom.chan.ui.viewmodel.RegisterEvent
import com.mycom.chan.ui.viewmodel.RegisterState
import com.mycom.chan.ui.viewmodel.RegisterViewModel

@Composable
fun Register(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    ) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.eventFlow.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess-> {
                    snackbarHostState.showSnackbar(
                        message = "Register Successfully"
                    )
                    onRegisterSuccess()
                }
                is RegisterEvent.ShowError -> {
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
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(400)) + 
                slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(400)) +
                scaleIn(initialScale = 0.9f, animationSpec = tween(400, easing = FastOutSlowInEasing)),
            exit = fadeOut(animationSpec = tween(400)) + 
                slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(400, easing = FastOutSlowInEasing)) +
                scaleOut(targetScale = 1.1f, animationSpec = tween(400)),

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
            Column (
                modifier = Modifier.width(345.dp).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegisterInput(
                    username = uiState.username,
                    email = uiState.email,
                    password = uiState.password,
                    confirmPassword = uiState.confirmPassword,
                    onUsernameChange = {viewModel.onUsernameChange(it)},
                    onEmailChange = {viewModel.onEmailChange(it)},
                    onPasswordChange = {viewModel.onPasswordChange(it)},
                    onConfirmPasswordChange = {viewModel.onConfirmPasswordChange(it)}
                )
                RegisterButton(
                    isLoading = uiState.isLoading,
                    onLoginClick = onNavigateToLogin,
                    onRegisterClick = {viewModel.register()}
                )
                
                // Snackbar for showing errors and success messages
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