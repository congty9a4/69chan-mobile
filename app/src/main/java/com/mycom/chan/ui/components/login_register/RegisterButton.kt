package com.mycom.chan.ui.components.login_register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterButton (
    isLoading: Boolean = false,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1D61E7)
            ),
        ) {
            Text(
                text = "SIGN UP",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier.clickable(onClick = onLoginClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Already have an account?",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                text = "Login",
                color = Color(0xFF2267ee),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }
}