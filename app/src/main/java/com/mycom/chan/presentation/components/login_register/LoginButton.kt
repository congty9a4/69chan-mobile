package com.mycom.chan.presentation.components.login_register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun LoginButton(
    isLoading: Boolean = false,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthPrimaryButton(
            text = "Log in",
            onClick = onLoginClick,
            isLoading = isLoading
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "or log in with",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OtherLoginButton(R.drawable.google_icon, onGoogleClick, "Google")
            OtherLoginButton(R.drawable.facebook_icon, onFacebookClick, "Facebook")
        }

        AuthSwitchPrompt(
            question = "Don't have an account?",
            actionLabel = "Sign up",
            onAction = onSignUpClick
        )
    }
}

@Composable
fun OtherLoginButton(
    id: Int,
    onclick: () -> Unit,
    providerName: String
) {
    OutlinedIconButton(
        onClick = onclick,
        modifier = Modifier.size(48.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        // Brand logos keep their own colors
        Icon(
            imageVector = ImageVector.vectorResource(id),
            contentDescription = "Log in with $providerName",
            tint = Color.Unspecified,
            modifier = Modifier.size(IconSize.small)
        )
    }
}
