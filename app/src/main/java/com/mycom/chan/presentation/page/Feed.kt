package com.mycom.chan.presentation.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.R
import com.mycom.chan.presentation.components.Menu
import com.mycom.chan.presentation.components.feed.PostCard
import com.mycom.chan.presentation.components.navbar.BottomNavBar
import com.mycom.chan.presentation.components.navbar.TopNavBar
import com.mycom.chan.presentation.viewmodel.FeedViewModel
import com.mycom.chan.presentation.viewmodel.LocalAuthViewModel
import com.mycom.chan.presentation.viewmodel.NotificationViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val notificationState by notificationViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= totalItems - 3 && totalItems > 0
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { viewModel.loadMore() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                Menu(
                    onNavigateToProfile = {
                        authState.currentUser?.id?.let { userId ->
                            onNavigateToProfile(userId)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold (
            topBar = { TopNavBar(
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
                     },
            bottomBar = { BottomNavBar(
                unreadNotificationCount = notificationState.unreadCount,
                onNavigateToHome = {},
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToMessages = onNavigateToMessages
            ) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToCreatePost,
                    containerColor = Color(0xff4C9EEB)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus),
                        contentDescription = "add-new-post",
                        tint = Color.White
                    )
                }
            }

        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        items(uiState.posts, key = { it.id }) { post ->
                            PostCard(
                                post = post,
                                onLike = { viewModel.toggleLike(post.id) },
                                onComment = { onNavigateToComments(post.id) }
                            )
                        }
                        if (uiState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
