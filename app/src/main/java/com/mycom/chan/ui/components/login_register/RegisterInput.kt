package com.mycom.chan.ui.components.login_register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R
import com.mycom.chan.ui.components.InputField

@Composable
fun RegisterInput(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(350.dp)
    ){
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = "login-screen-logo,",
            modifier = Modifier.size(34.dp)
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SIGN UP",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Create an account to continue!",
                fontSize = 12.sp
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputField(label = "Username",
                placeholder = "How should we call you?",
                value = username,
                onValueChange = onUsernameChange
            )
            InputField(label = "Email",
                placeholder = "Drop your email here!",
                value = email,
                onValueChange = onEmailChange
            )
            InputField(label = "Password",
                placeholder = "Enter your password.",
                isPassword = true,
                value = password,
                onValueChange = onPasswordChange
            )
            InputField(label = "Confirm Password", placeholder = "Making sure you enter it right!", isPassword = true)
        }
    }

}

