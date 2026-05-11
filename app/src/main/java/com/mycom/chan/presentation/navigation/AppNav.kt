package com.mycom.chan.presentation.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mycom.chan.presentation.page.ChatList
import com.mycom.chan.presentation.page.ChatPage
import com.mycom.chan.presentation.page.CommentCard
import com.mycom.chan.presentation.page.CreatePost
import com.mycom.chan.presentation.page.CreateProfileScreen
import com.mycom.chan.presentation.page.Feed
import com.mycom.chan.presentation.page.Login
import com.mycom.chan.presentation.page.Notifications
import com.mycom.chan.presentation.page.Profile
import com.mycom.chan.presentation.page.Register
import com.mycom.chan.presentation.page.Search
import com.mycom.chan.presentation.page.VerifyEmail
import com.mycom.chan.presentation.viewmodel.AuthViewModel
import com.mycom.chan.presentation.viewmodel.CreatePostEvent
import com.mycom.chan.presentation.viewmodel.CreatePostViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Feed : Screen("feed")
    object ForgotPassword : Screen("forgot_password")
    object VerifyEmail : Screen("verify_email/{email}") {
        fun createRoute(email: String) = "verify_email/$email"
    }
    object CreateProfile : Screen("create_profile")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
    object Search : Screen("search")
    object Notifications : Screen("notifications")
    object ChatList : Screen("chat_list")
    object Chat : Screen("chat/{conversationId}/{receiverId}") {
        fun createRoute(conversationId: String, receiverId: String) = "chat/$conversationId/$receiverId"
    }
    object Comments : Screen("comments/{postId}") {
        fun createRoute(postId: String) = "comments/$postId"
    }
    object CreatePost : Screen("create_post_screen")
}

@Composable
fun AppNav(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(authState.isLoggedIn, authState.profileExists) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        val onAuthScreen = currentRoute in listOf(
            Screen.Login.route, Screen.Register.route,
            Screen.VerifyEmail.route, Screen.ForgotPassword.route
        )
        if (!authState.isLoggedIn && !authState.isLoading && !onAuthScreen) {
            // Token expired or was cleared — redirect to Login from wherever we are
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        } else if (authState.isLoggedIn && authState.profileExists != null && onAuthScreen) {
            if (authState.profileExists == true) {
                navController.navigate(Screen.Feed.route) {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.CreateProfile.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Screen.Login.route) {
            Login(
                onLoginSuccess = {},
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }

        composable(route = Screen.Register.route) {
            Register(
                onRegisterSuccess = { email ->
                    navController.navigate(Screen.VerifyEmail.createRoute(email)) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(route = Screen.VerifyEmail.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyEmail(
                email = email,
                onVerifySuccess = {}
            )
        }

        composable(route = Screen.CreateProfile.route) {
            val userId = authState.currentUser?.id ?: ""
            CreateProfileScreen(
                userId = userId,
                onProfileCreated = {
                    authViewModel.onProfileCreated()
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    authViewModel.logout()
                    // logout() clears the token → observeAuthFlow → isLoggedIn = false
                    // → AppNav LaunchedEffect redirects to Login automatically
                }
            )
        }

        composable(route = Screen.Feed.route) {
            Feed(
                onNavigateToComments = { postId -> navController.navigate(Screen.Comments.createRoute(postId)) },
                onNavigateToCreatePost = { navController.navigate(Screen.CreatePost.route) },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onNavigateToMessages = { navController.navigate(Screen.ChatList.route) }
            )
        }

        composable(route = Screen.Profile.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val isOwn = userId == authState.currentUser?.id
            Profile(
                userId = userId,
                isOwnProfile = isOwn,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Search.route) {
            Search(
                onBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) }
            )
        }

        composable(route = Screen.Notifications.route) {
            Notifications(
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ChatList.route) {
            ChatList(
                onBackToMainPage = { navController.popBackStack() },
                onChatItemClick = { conversationId, receiverId ->
                    navController.navigate(Screen.Chat.createRoute(conversationId, receiverId))
                }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("receiverId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            ChatPage(
                conversationId = conversationId,
                currentUserId = authState.currentUser?.id ?: "",
                receiverId = receiverId,
                receiverName = receiverId, // TODO: thay bằng username thật khi ChatList truyền thêm tên
                receiverAvatar = null,
                onNavigateToChatList = { navController.popBackStack() }
            )
        }

        composable(route = Screen.CreatePost.route) {
            val createPostViewModel: CreatePostViewModel = hiltViewModel()
            val uiState by createPostViewModel.uiState.collectAsState()
            val currentUser = authState.currentUser ?: return@composable

            val mediaPicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetMultipleContents()
            ) { uris -> uris.forEach { createPostViewModel.addUri(it) } }

            LaunchedEffect(Unit) {
                createPostViewModel.eventChannel.collect { event ->
                    when (event) {
                        CreatePostEvent.CreateSuccess -> navController.popBackStack()
                        is CreatePostEvent.ShowError -> {}
                    }
                }
            }

            CreatePost(
                onCancelPost = { navController.popBackStack() },
                onCreatePost = { createPostViewModel.createPost() },
                isPostAble = !uiState.isLoading && uiState.caption.isNotBlank(),
                user = currentUser,
                onAddImage = { mediaPicker.launch("image/*") },
                onAddVideo = { mediaPicker.launch("video/*") },
                text = uiState.caption,
                onTextChange = createPostViewModel::onCaptionChange,
                selectedUris = uiState.selectedUris
            )
        }

        composable(
            route = Screen.Comments.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            CommentCard(
                postId = postId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ForgotPassword.route) {
            // ForgotPassword()
        }
    }
}
