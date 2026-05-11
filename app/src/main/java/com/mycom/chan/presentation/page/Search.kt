package com.mycom.chan.presentation.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.mycom.chan.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.domain.model.Post
import com.mycom.chan.domain.model.UserSearchResult
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.components.feed.PostCard
import com.mycom.chan.presentation.theme.ChanTheme
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

@Composable
private fun SearchContent(
    uiState: SearchState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFilterChange: (SearchFilter) -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(ImageVector.vectorResource(R.drawable.back), contentDescription = null)
            }
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search…") },
                leadingIcon = { Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                ) },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xff4C9EEB),
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }

        // Filter tabs
        TabRow(
            selectedTabIndex = uiState.filter.ordinal,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = uiState.filter == SearchFilter.ALL,
                onClick = { onFilterChange(SearchFilter.ALL) },
                text = { Text("Top") }
            )
            Tab(
                selected = uiState.filter == SearchFilter.POSTS,
                onClick = { onFilterChange(SearchFilter.POSTS) },
                text = { Text("Posts") }
            )
            Tab(
                selected = uiState.filter == SearchFilter.PEOPLE,
                onClick = { onFilterChange(SearchFilter.PEOPLE) },
                text = { Text("People") }
            )
        }

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Users section
            if (uiState.filter != SearchFilter.POSTS && uiState.users.isNotEmpty()) {
                items(uiState.users, key = { "user_${it.id}" }) { user ->
                    UserSearchRow(
                        user = user,
                        onClick = { onNavigateToProfile(user.id) }
                    )
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
            // Posts section
            if (uiState.filter != SearchFilter.PEOPLE && uiState.posts.isNotEmpty()) {
                items(uiState.posts, key = { "post_${it.id}" }) { post ->
                    PostCard(post = post)
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
            // Empty state
            if (!uiState.isLoading && uiState.query.isNotBlank()
                && uiState.users.isEmpty() && uiState.posts.isEmpty()
            ) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("No results for \"${uiState.query}\"", color = Color.Gray) }
                }
            }
        }
    }
}

@Composable
private fun UserSearchRow(
    user: UserSearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Avatar(
            imageUrl = null,
            modifier = Modifier.size(44.dp).clip(CircleShape)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(user.userName, fontWeight = FontWeight.Bold)
            Text(user.email, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp)
        ) { Text("View") }
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