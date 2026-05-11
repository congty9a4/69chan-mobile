package com.mycom.chan.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R
import com.mycom.chan.domain.model.User
import com.mycom.chan.presentation.viewmodel.AuthViewModel
import com.mycom.chan.presentation.viewmodel.LocalAuthViewModel

@Composable
fun Menu(
    viewModel: AuthViewModel = LocalAuthViewModel.current,
    onNavigateToProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header với thông tin user
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else {
            val currentUser = uiState.currentUser
            if (uiState.isLoggedIn && currentUser != null) {
                HeaderTopBar(
                    avatarUrl = currentUser.avatarUrl,
                    onNavigateToProfile = onNavigateToProfile)
                HeaderUserInfo(user = currentUser)
            } else {
                Text(
                    text = "Vui lòng đăng nhập",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        
        // Menu items
        MenuNavItem(R.drawable.message, "message", "Message")
        MenuNavItem(R.drawable.notification, "message", "Notifications")
        MenuNavItem(R.drawable.saved, "message", "Saved Post")
        MenuNavItem(R.drawable.setting, "message", "Settings")
        MenuNavItem(R.drawable.customer_service, "message", "Customer Service")

        Spacer(Modifier.weight(1f))

        // Nút Logout nếu đã đăng nhập
        if (uiState.isLoggedIn) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            MenuNavItem(
                id = R.drawable.message,
                description = "logout",
                name = "Đăng xuất",
                onClick = {
                    viewModel.logout()
                    onLogout()
                }
            )
        }
    }
}


@Composable
fun HeaderTopBar(
    avatarUrl: String?,
    onNavigateToProfile: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Avatar(
            imageUrl = avatarUrl,
            modifier = Modifier.size(48.dp),
            isEditable = false,
            onClick = onNavigateToProfile
        )
    }
}

@Composable
fun HeaderUserInfo(user: User?) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = user?.fullName ?: user?.userName ?: "Unknown",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "@${user?.userName ?: "guest"}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun MenuNavItem(
    id: Int,
    description: String,
    name: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id),
            contentDescription = "${description}-navigation-item",
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}