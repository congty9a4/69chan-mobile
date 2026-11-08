package com.mycom.chan.presentation.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.presentation.components.login_register.AuthHeader
import com.mycom.chan.presentation.components.login_register.AuthPrimaryButton
import com.mycom.chan.presentation.components.login_register.AuthScreenContainer
import com.mycom.chan.presentation.viewmodel.VerifyEmailEvent
import com.mycom.chan.presentation.viewmodel.VerifyEmailViewModel

private const val OTP_LENGTH = 6

@Composable
fun VerifyEmail(
    email: String,
    viewModel: VerifyEmailViewModel = hiltViewModel(),
    onVerifySuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val canVerify = !uiState.isLoading && uiState.otp.length == OTP_LENGTH

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is VerifyEmailEvent.VerifySuccess -> onVerifySuccess()
                is VerifyEmailEvent.ResendSuccess -> snackbarHostState.showSnackbar("Code sent to $email")
                is VerifyEmailEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    AuthScreenContainer(snackbarHostState = snackbarHostState, enterFromStart = true) {
        AuthHeader(
            title = "Verify your email",
            subtitle = "Enter the $OTP_LENGTH-digit code we sent to\n$email"
        )
        OutlinedTextField(
            value = uiState.otp,
            // Digits only, at most 6
            onValueChange = { input ->
                if (input.length <= OTP_LENGTH && input.all(Char::isDigit)) viewModel.onOtpChange(input)
            },
            label = { Text("Verification code") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { if (canVerify) viewModel.verify(email) }),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.SmsOtpCode }
        )
        AuthPrimaryButton(
            text = "Verify",
            onClick = { viewModel.verify(email) },
            enabled = uiState.otp.length == OTP_LENGTH,
            isLoading = uiState.isLoading
        )
        TextButton(onClick = { viewModel.resendOtp(email) }) {
            Text("Resend code")
        }
    }
}
