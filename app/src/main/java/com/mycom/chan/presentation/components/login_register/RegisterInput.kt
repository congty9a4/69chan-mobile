package com.mycom.chan.presentation.components.login_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.mycom.chan.presentation.components.InputField
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun RegisterInput(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    // Full width of the card instead of a fixed 350dp, which overflowed on small phones
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AuthHeader(
            title = "Sign up",
            subtitle = "Create an account to continue!"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            InputField(
                label = "Username",
                placeholder = "How should we call you?",
                value = username,
                onValueChange = onUsernameChange,
                autofillType = ContentType.NewUsername
            )
            InputField(
                label = "Email",
                placeholder = "you@example.com",
                value = email,
                onValueChange = onEmailChange,
                keyboardType = KeyboardType.Email,
                autofillType = ContentType.EmailAddress
            )
            InputField(
                label = "Password",
                placeholder = "Enter your password",
                isPassword = true,
                value = password,
                onValueChange = onPasswordChange,
                autofillType = ContentType.NewPassword
            )
            InputField(
                label = "Confirm password",
                placeholder = "Type it once more",
                isPassword = true,
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                autofillType = ContentType.NewPassword
            )
        }
    }
}
