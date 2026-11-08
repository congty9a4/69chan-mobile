package com.mycom.chan.presentation.components.login_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.mycom.chan.presentation.components.InputField
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun LoginInput(
    email: String,
    password: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AuthHeader(
            title = "Log in",
            subtitle = "Enter your email and password to log in."
        )
        LoginInputField(
            email = email,
            password = password,
            rememberMe = rememberMe,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onRememberMeChange = onRememberMeChange,
            onForgotPasswordClick = onForgotPasswordClick,
            onSubmit = onSubmit
        )
    }
}

@Composable
fun LoginInputField(
    email: String,
    password: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSubmit: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        InputField(
            label = "Email",
            placeholder = "you@example.com",
            value = email,
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            autofillType = ContentType.EmailAddress
        )
        InputField(
            label = "Password",
            placeholder = "Enter your password",
            isPassword = true,
            value = password,
            onValueChange = onPasswordChange,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { onSubmit() }),
            autofillType = ContentType.Password
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // The whole row (box + label) toggles, and is announced as one checkbox
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .toggleable(
                        value = rememberMe,
                        role = Role.Checkbox,
                        onValueChange = onRememberMeChange
                    )
                    .minimumInteractiveComponentSize()
                    .padding(end = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = rememberMe, onCheckedChange = null)
                Spacer(Modifier.width(Spacing.sm))
                Text(
                    text = "Remember me",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            TextButton(onClick = onForgotPasswordClick) {
                Text("Forgot password?")
            }
        }
    }
}
