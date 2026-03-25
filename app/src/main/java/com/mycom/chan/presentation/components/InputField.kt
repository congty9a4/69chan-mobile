package com.mycom.chan.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mycom.chan.R

@Composable
fun InputField(
    label: String,
    placeholder: String,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isPassword: Boolean = false,
) {
    var passwordVisible by remember {mutableStateOf(false)}
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth(),
        visualTransformation =
            if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            if (isPassword) {
                val image =
                    if (passwordVisible) {
                        ImageVector.vectorResource(R.drawable.eye_open)
                    } else {
                        ImageVector.vectorResource(R.drawable.eye_closed)
                    }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "toggle-password-visible")
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}