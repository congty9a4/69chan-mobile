package com.mycom.chan.ui.components.login_register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R

@Composable
fun LoginButton(
    isLoading: Boolean = false,
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1D61E7)
            ),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "LOGIN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = "Or login with",
            fontSize = 10.sp,
            color = Color(0xff6C7278)
        )
        Row (
            modifier = Modifier.height(48.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OtherLoginButton(R.drawable.google_icon, onGoogleClick, "google")
            OtherLoginButton(R.drawable.facebook_icon, onFacebookClick, "facebook")
        }
        Row (
            modifier = Modifier.clickable(onClick = onSignUpClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Don’t have an account?",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                text = "Sign up.",
                color = Color(0xFF2267ee),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun OtherLoginButton (
    id: Int,
    onclick:() -> Unit,
    description: String
) {
    IconButton(onclick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.White,
            contentColor = Color.Unspecified
        )) {
        Icon(
            imageVector = ImageVector.vectorResource(id),
            modifier = Modifier.size(18.dp),
            contentDescription = "login-with-${description}",
            tint = Color.Unspecified
            )
    }
}