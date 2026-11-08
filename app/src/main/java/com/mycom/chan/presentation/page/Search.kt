package com.mycom.chan.presentation.page

import com.mycom.chan.presentation.components.BackButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.R
import com.mycom.chan.domain.model.UserSearchResult
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.components.feed.PostCard
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.viewmodel.SearchFilter
import com.mycom.chan.presentation.viewmodel.SearchState
import com.mycom.chan.presentation.viewmodel.SearchViewModel

@Composable
fun Search(
    onBack: () -> Unit,
    onNavigateToProfile: (userId: String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchContent(
        uiState = uiState,
        onBack = onBack,
        onQueryChange = viewModel::onQueryChange,
        onFilterChange = viewModel::onFilterChange,
        onNavigateToProfile = onNavigateToProfile
    )
}

private val searchTabs = listOf(
    SearchFilter.ALL to "Top",
    SearchFilter.POSTS to "Posts",
    SearchFilter.PEOPLE to "People"
)

@Composable
internal fun SearchContent(
    uiState: SearchState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFilterChange: (SearchFilter) -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = Spacing.xs, end = Spacing.lg, top = Spacing.sm, bottom = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                BackButton(onClick = onBack)
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search people and posts") },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.search),
                            contentDescription = null,
                            modifier = Modifier.size(IconSize.small)
                        )
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }

            PrimaryTabRow(selectedTabIndex = uiState.filter.ordinal) {
                searchTabs.forEach { (filter, label) ->
                    Tab(
                        selected = uiState.filter == filter,
                        onClick = { onFilterChange(filter) },
                        text = { Text(label) }
                    )
                }
            }

            when {
                uiState.isLoading -> CenteredMessage { CircularProgressIndicator() }

                uiState.query.isBlank() -> CenteredMessage {
                    Text(
                        text = "Find people by username, or posts by what they say.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                uiState.users.isEmpty() && uiState.posts.isEmpty() -> CenteredMessage {
                    Text(
                        text = "No results for \"${uiState.query}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    // Last result scrolls clear of the gesture bar
                    contentPadding = WindowInsets.navigationBars.asPaddingValues()
                ) {
                    if (uiState.filter != SearchFilter.POSTS) {
                        items(uiState.users, key = { "user_${it.id}" }) { user ->
                            UserSearchRow(
                                user = user,
                                onClick = { onNavigateToProfile(user.id) }
                            )
                            HorizontalDivider()
                        }
                    }
                    if (uiState.filter != SearchFilter.PEOPLE) {
                        items(uiState.posts, key = { "post_${it.id}" }) { post ->
                            PostCard(post = post)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CenteredMessage(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.xxl),
        contentAlignment = Alignment.Center
    ) { content() }
}

/** The whole row opens the profile, so the tap target is the full width. */
@Composable
private fun UserSearchRow(
    user: UserSearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClickLabel = "Open profile", onClick = onClick)
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Avatar(
            imageUrl = null,
            modifier = Modifier.size(AvatarSize.large)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.userName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Search — Empty")
@Composable
private fun SearchEmptyPreview() {
    ChanTheme {
        SearchContent(
            uiState = SearchState(),
            onBack = {}, onQueryChange = {}, onFilterChange = {}, onNavigateToProfile = {}
        )
    }
}

@Preview(showBackground = true, name = "Search — Results")
@Composable
private fun SearchResultsPreview() {
    ChanTheme {
        SearchContent(
            uiState = SearchState(
                query = "chan",
                users = listOf(
                    UserSearchResult("1", "chan_user", "chan@email.com"),
                    UserSearchResult("2", "nerd_dev", "nerd@email.com")
                )
            ),
            onBack = {}, onQueryChange = {}, onFilterChange = {}, onNavigateToProfile = {}
        )
    }
}
