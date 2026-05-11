package com.mycom.chan.presentation.util

import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel

fun List<MessageUiModel>.calculatePositions(): List<MessageUiModel> {
    if (this.isEmpty()) return emptyList()

    return this.mapIndexed { index, currentMessage ->
        val prevMessage: MessageUiModel? = this.getOrNull(index - 1)
        val nextMessage: MessageUiModel? = this.getOrNull(index + 1)

        val isFromSameSenderAsPrev = prevMessage?.isMine == currentMessage.isMine
        val isFromSameSenderAsNext = nextMessage?.isMine == currentMessage.isMine

        val newPosition = when {
            !isFromSameSenderAsPrev && isFromSameSenderAsNext -> MessagePosition.TOP
            isFromSameSenderAsPrev && isFromSameSenderAsNext -> MessagePosition.MIDDLE
            isFromSameSenderAsPrev && !isFromSameSenderAsNext -> MessagePosition.BOTTOM
            else -> MessagePosition.SINGLE
        }

        currentMessage.copy(position = newPosition)
    }
}