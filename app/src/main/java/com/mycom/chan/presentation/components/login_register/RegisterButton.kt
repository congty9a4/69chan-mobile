package com.mycom.chan.presentation.components.login_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun RegisterButton(
    isLoading: Boolean = false,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthPrimaryButton(
            text = "Sign up",
            onClick = onRegisterClick,
            isLoading = isLoading
        )
        AuthSwitchPrompt(
            question = "Already have an account?",
            actionLabel = "Log in",
            onAction = onLoginClick
        )
    }
}
