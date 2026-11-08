package com.mycom.chan.presentation.page

import com.mycom.chan.presentation.components.BackButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.R
import com.mycom.chan.domain.model.Comment
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.util.formatRelativeTime
import com.mycom.chan.presentation.viewmodel.CommentsState
import com.mycom.chan.presentation.viewmodel.CommentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentCard(
    postId: String,
    onBack: () -> Unit,
    viewModel: CommentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(postId) {
        viewModel.init(postId)
    }

    CommentsContent(
        uiState = uiState,
        onBack = onBack,
        onInputChange = viewModel::onInputChange,
        onSend = viewModel::submitComment
    )
}

/** Comments UI without the ViewModel, so the debug preview screen can show it with sample data. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommentsContent(
    uiState: CommentsState,
    onBack: () -> Unit,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comments") },
                navigationIcon = {
                    BackButton(onClick = onBack)
                }
            )
        },
        bottomBar = {
            CommentInputBar(
                text = uiState.inputText,
                isSending = uiState.isSending,
                onTextChange = onInputChange,
                onSend = onSend
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            uiState.comments.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(Spacing.xl),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No comments yet. Be the first to comment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.lg)
            ) {
                items(uiState.comments, key = { it.id }) { comment ->
                    CommentItem(comment = comment)
                }
            }
        }
    }
}

@Composable
private fun CommentInputBar(
    text: String,
    isSending: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Write a comment…") },
                    shape = MaterialTheme.shapes.extraLarge,
                    maxLines = 4
                )
                FilledIconButton(
                    onClick = onSend,
                    enabled = text.isNotBlank() && !isSending
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(IconSize.small),
                            strokeWidth = Spacing.xxs
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.send),
                            contentDescription = "Send comment",
                            modifier = Modifier.size(IconSize.small)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Avatar(
            modifier = Modifier.size(AvatarSize.small),
            imageUrl = comment.user.avatarUrl
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.user.fullName.ifBlank { comment.user.userName },
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = "@${comment.user.userName} · ${formatRelativeTime(comment.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(text = comment.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
