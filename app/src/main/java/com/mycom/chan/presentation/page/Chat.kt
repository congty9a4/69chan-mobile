package com.mycom.chan.presentation.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.presentation.components.chat.ChatContainer
import com.mycom.chan.presentation.components.chat.ChatMessageBar
import com.mycom.chan.presentation.components.chat.ChatNavigationBar
import com.mycom.chan.presentation.uimodel.MessagePosition
import com.mycom.chan.presentation.uimodel.MessageUiModel
import com.mycom.chan.presentation.viewmodel.ChatState
import com.mycom.chan.presentation.viewmodel.ChatViewModel

@Composable
fun ChatPage(
    conversationId: String,
    currentUserId: String,
    receiverId: String,
    receiverName: String,
    receiverAvatar: String?,
    onNavigateToChatList: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(conversationId) {
        viewModel.init(conversationId, currentUserId, receiverId)
    }
    ChatContent(
        receiverName = receiverName,
        receiverAvatar = receiverAvatar,
        uiState = uiState,
        onBack = onNavigateToChatList,
        onInputChange = viewModel::onInputChange,
        onSend = viewModel::sendMessage
    )
}

/** Conversation UI without the ViewModel, so the debug preview screen can show it with sample data. */
@Composable
internal fun ChatContent(
    receiverName: String,
    receiverAvatar: String?,
    uiState: ChatState,
    onBack: () -> Unit,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Scaffold(
        topBar = {
            ChatNavigationBar(
                senderName = receiverName,
                senderAvatar = receiverAvatar,
                onNavigateToChatList = onBack
            )
        },
        bottomBar = {
            ChatMessageBar(
                text = uiState.inputText,
                onTextChange = onInputChange,
                onSendMessage = onSend
            )
        }
    ) { innerPadding ->
        ChatContainer(
            messages = uiState.messages,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

