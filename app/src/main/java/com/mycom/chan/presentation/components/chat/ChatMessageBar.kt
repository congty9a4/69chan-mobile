package com.mycom.chan.presentation.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing

@Composable
fun ChatMessageBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onStickerClick: () -> Unit = {}
) {
    val canSend = text.isNotBlank()

    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // Stay above the gesture bar, and above the keyboard while typing
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message") },
                    maxLines = 5,
                    shape = MaterialTheme.shapes.extraLarge,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(onSend = { if (canSend) onSendMessage() }),
                    trailingIcon = {
                        IconButton(onClick = onStickerClick) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.sticker),
                                contentDescription = "Stickers",
                                modifier = Modifier.size(IconSize.medium)
                            )
                        }
                    }
                )
                FilledIconButton(
                    onClick = onSendMessage,
                    enabled = canSend
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.send),
                        contentDescription = "Send message",
                        modifier = Modifier.size(IconSize.small)
                    )
                }
            }
        }
    }
}
