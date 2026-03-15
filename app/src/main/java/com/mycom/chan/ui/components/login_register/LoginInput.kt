package com.mycom.chan.ui.components.login_register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R
import com.mycom.chan.ui.components.InputField
import com.mycom.chan.ui.viewmodel.LoginState

@Composable
fun LoginInput(
    email: String,
    password: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit = {}
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = "login-screen-logo,",
            modifier = Modifier.size(34.dp)
        )
        LoginGreetingText()
        LoginInputField(
            email = email,
            password = password,
            rememberMe = rememberMe,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onRememberMeChange = onRememberMeChange,
            onForgotPasswordClick = onForgotPasswordClick
        )
    }
}

@Composable
fun LoginGreetingText() {
    Column (
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LOGIN",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter your email and password to log in.",
            fontSize = 12.sp
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
    onForgotPasswordClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputField(
            label = "Email",
            placeholder = "Your email goes here",
            value = email,
            onValueChange = onEmailChange
        )
        InputField(
            label = "Password",
            placeholder = "Enter your password",
            isPassword = true,
            value = password,
            onValueChange = onPasswordChange
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row (
                modifier = Modifier
                    .clickable {
                        onRememberMeChange(!rememberMe)
                    }
                    .offset(x = (-8).dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = onRememberMeChange,
                )
                Text(
                    text = "Remember me.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.offset(x = (-8).dp)
                )
            }
            Text(
                text = "Forgot Password?",
                fontSize = 14.sp,
                modifier = Modifier.clickable { onForgotPasswordClick() }
            )
        }
    }
}

