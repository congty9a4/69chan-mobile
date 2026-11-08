package com.mycom.chan.presentation.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.R
import com.mycom.chan.presentation.components.Menu
import com.mycom.chan.presentation.components.feed.PostCard
import com.mycom.chan.presentation.components.navbar.BottomNavBar
import com.mycom.chan.presentation.components.navbar.BottomNavDestination
import com.mycom.chan.presentation.components.navbar.TopNavBar
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.viewmodel.AuthState
import com.mycom.chan.presentation.viewmodel.FeedState
import com.mycom.chan.presentation.viewmodel.FeedViewModel
import com.mycom.chan.presentation.viewmodel.LocalAuthViewModel
import com.mycom.chan.presentation.viewmodel.NotificationViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun Feed(
    viewModel: FeedViewModel = hiltViewModel(),
    onNavigateToComments: (String) -> Unit = {},
    onNavigateToCreatePost: () -> Unit = {},
    onNavigateToProfile: (userId: String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    val authViewModel = LocalAuthViewModel.current
    val authState by authViewModel.uiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val notificationState by notificationViewModel.uiState.collectAsState()

    FeedContent(
        uiState = uiState,
        authState = authState,
        unreadNotificationCount = notificationState.unreadCount,
        onLike = viewModel::toggleLike,
        onLoadMore = viewModel::loadMore,
        onLogout = authViewModel::logout,
        onNavigateToComments = onNavigateToComments,
        onNavigateToCreatePost = onNavigateToCreatePost,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToMessages = onNavigateToMessages
    )
}

/** Space under the last post: 56dp + button plus its 16dp margin, and a little air. */
private val FabClearance = 88.dp

/**
 * Material guidance for a modal drawer: at most 360dp wide, and always leaving at least
 * 56dp of the screen uncovered so the scrim stays visible and tapping it closes the drawer.
 * Without this the drawer filled the whole width on 360dp phones.
 */
private val DrawerMaxWidth = 360.dp
private val DrawerEdgeGap = 56.dp

/**
 * The feed UI without ViewModels: it only draws the state it is given,
 * so the debug preview screen can show it with sample data.
 */
@Composable
internal fun FeedContent(
    uiState: FeedState,
    authState: AuthState,
    unreadNotificationCount: Long,
    onLike: (postId: String) -> Unit,
    onLoadMore: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToComments: (String) -> Unit,
    onNavigateToCreatePost: () -> Unit,
    onNavigateToProfile: (userId: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToMessages: () -> Unit,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    // Close the drawer first, then leave the screen
    fun closeDrawerThen(action: () -> Unit) {
        scope.launch {
            drawerState.close()
            action()
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 3 && totalItems > 0
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { currentOnLoadMore() }
    }

    BoxWithConstraints {
        val drawerWidth = min(DrawerMaxWidth, maxWidth - DrawerEdgeGap).coerceAtLeast(0.dp)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.widthIn(max = drawerWidth)) {
                    Menu(
                        authState = authState,
                        onNavigateToProfile = {
                            authState.currentUser?.id?.let { userId ->
                                closeDrawerThen { onNavigateToProfile(userId) }
                            }
                        },
                        onNavigateToMessages = { closeDrawerThen(onNavigateToMessages) },
                        onNavigateToNotifications = { closeDrawerThen(onNavigateToNotifications) },
                        onLogout = onLogout
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopNavBar(onMenuClick = { scope.launch { drawerState.open() } })
                },
                bottomBar = {
                    BottomNavBar(
                        selected = BottomNavDestination.HOME,
                        unreadNotificationCount = unreadNotificationCount,
                        // Already on Home: scroll back to the newest post
                        onNavigateToHome = { scope.launch { listState.animateScrollToItem(0) } },
                        onNavigateToSearch = onNavigateToSearch,
                        onNavigateToNotifications = onNavigateToNotifications,
                        onNavigateToMessages = onNavigateToMessages
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onNavigateToCreatePost,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.plus),
                            contentDescription = "Create post",
                            modifier = Modifier.size(IconSize.medium)
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when {
                        uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                        uiState.posts.isEmpty() -> Text(
                            text = "No posts yet. Tap + to write the first one.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(Spacing.xl)
                        )

                        else -> LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            // Lets the last post scroll clear of the + button
                            contentPadding = PaddingValues(bottom = FabClearance)
                        ) {
                            items(uiState.posts, key = { it.id }) { post ->
                                PostCard(
                                    post = post,
                                    onLike = { onLike(post.id) },
                                    onComment = { onNavigateToComments(post.id) }
                                )
                                HorizontalDivider()
                            }
                            if (uiState.isLoadingMore) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(Spacing.lg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(IconSize.medium))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
