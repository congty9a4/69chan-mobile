package com.mycom.chan.presentation.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.presentation.components.chat.ChatListContainer
import com.mycom.chan.presentation.components.chat.ChatListNavigationBar
import com.mycom.chan.presentation.uimodel.ChatFilter
import com.mycom.chan.presentation.uimodel.ChatListItemUiModel
import com.mycom.chan.presentation.viewmodel.ChatListViewModel
import com.mycom.chan.presentation.viewmodel.LocalAuthViewModel

@Composable
fun ChatList(
    onBackToMainPage: () -> Unit,
    onChatItemClick: (conversationId: String, receiverId: String) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val authViewModel = LocalAuthViewModel.current
    val authState by authViewModel.uiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val currentUserId = authState.currentUser?.id ?: ""
    val chatItems = viewModel.chatItems(currentUserId)

    ChatListContent(
        isLoading = uiState.isLoading,
        filter = uiState.filter,
        chatItems = chatItems,
        onBack = onBackToMainPage,
        onFilterSelected = viewModel::setFilter,
        onChatItemClick = { conversationId ->
            // receiverId = participant khác với currentUser
            val conv = uiState.conversations.find { it.id == conversationId }
            val receiverId = conv?.participantIds?.find { it != currentUserId } ?: ""
            onChatItemClick(conversationId, receiverId)
        }
    )
}

/** Conversation list UI without the ViewModel, so the debug preview screen can show it with sample data. */
@Composable
internal fun ChatListContent(
    isLoading: Boolean,
    filter: ChatFilter,
    chatItems: List<ChatListItemUiModel>,
    onBack: () -> Unit,
    onFilterSelected: (ChatFilter) -> Unit,
    onChatItemClick: (conversationId: String) -> Unit,
    onNewContact: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            ChatListNavigationBar(
                onBackToMainPage = onBack,
                modifier = Modifier,
                currentFilter = filter,
                onFilterSelected = onFilterSelected,
                onNewContact = onNewContact
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        } else {
            ChatListContainer(
                modifier = Modifier.padding(innerPadding),
                chatItems = chatItems,
                onChatItemClick = onChatItemClick
            )
        }
    }
}

