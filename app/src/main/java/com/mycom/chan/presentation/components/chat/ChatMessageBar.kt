package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.mycom.chan.R

@Composable
fun ChatMessageBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row (modifier = Modifier
        .fillMaxWidth()
        // .statusBarsPadding() // Dòng này đã được xóa
        .border(width = 2.dp,
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xffE2E8EE),
                    0.3f to Color(0xffE2E8EE),
                    0.7f to Color.Black,
                ),
                start = Offset(0f,0f),
                end = Offset(Float.POSITIVE_INFINITY,0f)
            ),
            shape = RectangleShape
        )
        .padding(10.dp)
        .heightIn(min = 45.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Message") },
            maxLines = 5,
            shape = RoundedCornerShape(16.5.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(onSend = { onSendMessage() }),
            trailingIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.sticker),
                        contentDescription = "sticker"
                    )
                }
            }
        )
        Button(
            onClick = onSendMessage,
            modifier = Modifier.heightIn(min = 50.dp),
//                enabled = TODO(),
//                shape = TODO(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff555e65),
                contentColor = Color.White,
            ),
//                elevation = TODO(),
//                border = TODO(),
            contentPadding = PaddingValues(horizontal = 8.dp),
//                interactionSource = TODO()
        ) {
            Text(
                text = "Send",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = ImageVector.vectorResource(R.drawable.enter),
                contentDescription = "send-message"
            )
        }
    }
}