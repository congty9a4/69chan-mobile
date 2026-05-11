package com.mycom.chan.presentation.components.create_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.domain.model.User

@Composable
fun TopCreatePostBar (
    onCancelPost: () -> Unit,
    onCreatePost: () -> Unit,
    isPostAble: Boolean,
    user: User
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = onCancelPost
        ) {
            Text(text = "Cancel",
                color = Color(0xff4C9EEB),
                textDecoration = TextDecoration.Underline,
                fontSize = 15.sp
            )
        }
        Text(
            text = buildAnnotatedString {
                append("New post of ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(user.userName)
                }
            },
            fontSize = 15.sp
        )
        Button(
            onClick = onCreatePost,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor=Color(0xffB9DCF7),
                containerColor = Color(0xff4C9EEB)
            ),
            enabled = isPostAble
            ) {
            Text("Post",
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}