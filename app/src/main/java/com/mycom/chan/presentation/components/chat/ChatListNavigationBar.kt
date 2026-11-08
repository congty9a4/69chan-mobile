package com.mycom.chan.presentation.components.chat

import com.mycom.chan.presentation.components.BackButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.uimodel.ChatFilter

/** Chat list top bar: back, title and "new chat", with the All / Unread filter underneath. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListNavigationBar(
    onBackToMainPage: () -> Unit,
    modifier: Modifier = Modifier,
    currentFilter: ChatFilter,
    onFilterSelected: (ChatFilter) -> Unit,
    onNewContact: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            title = { Text("Messages") },
            navigationIcon = {
                BackButton(onClick = onBackToMainPage)
            },
            actions = {
                IconButton(onClick = onNewContact) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.add_chat),
                        contentDescription = "New conversation",
                        modifier = Modifier.size(IconSize.medium)
                    )
                }
            }
        )
        FilterToggle(
            currentFilter = currentFilter,
            onFilterSelected = onFilterSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.sm)
        )
    }
}

@Composable
fun FilterToggle(
    currentFilter: ChatFilter,
    onFilterSelected: (ChatFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(ChatFilter.ALL to "All", ChatFilter.UNREAD to "Unread")
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, (filter, label) ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onFilterSelected(filter) },
                selected = currentFilter == filter
            ) {
                Text(label)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListNavigationBarPreview() {
    ChanTheme {
        ChatListNavigationBar(
            onBackToMainPage = {},
            currentFilter = ChatFilter.ALL,
            onFilterSelected = {},
            onNewContact = {}
        )
    }
}
