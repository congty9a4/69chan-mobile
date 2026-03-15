package com.mycom.chan.ui.components

import android.icu.text.CompactDecimalFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.R
import com.mycom.chan.data.remote.dto.model.UserDTO
import com.mycom.chan.ui.viewmodel.AuthViewModel
import java.util.Locale

@Composable
fun Menu(
    viewModel: AuthViewModel = hiltViewModel(),
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
        } else if (uiState.isLoggedIn && uiState.currentUser != null) {
            HeaderTopBar(user = uiState.currentUser)
            HeaderUserInfo(user = uiState.currentUser)
            HeaderFollowInfo()
        } else {
            // Trạng thái chưa đăng nhập
            Text(
                text = "Vui lòng đăng nhập",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        
        // Menu items
        MenuNavItem(R.drawable.message, "message", "Tin nhắn")
        MenuNavItem(R.drawable.message, "message", "Thông báo")
        MenuNavItem(R.drawable.message, "message", "Đã lưu")
        MenuNavItem(R.drawable.message, "message", "Cài đặt")
        MenuNavItem(R.drawable.message, "message", "Trợ giúp")
        
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
@Preview
fun MenuPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        HeaderTopBar(user = null)
        HeaderUserInfo(user = null)
        HeaderFollowInfo()
        MenuNavItem(R.drawable.message, "message", "Message")
    }
}

@Composable
fun HeaderTopBar(user: UserDTO?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Avatar chính của user
        Avatar(modifier = Modifier.size(48.dp))
        // Icon settings hoặc notification
        Avatar(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun HeaderUserInfo(user: UserDTO?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = user?.fullname ?: user?.username ?: "Unknown",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "@${user?.username ?: "guest"}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HeaderFollowInfo() {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        FollowStatItem("Following", 1000000)
        FollowStatItem("Followers",12345678)
    }
}

@Composable
fun FollowStatItem (label: String, count: Int) {
    val format = remember {
        CompactDecimalFormat.getInstance(
            Locale.US,
            CompactDecimalFormat.CompactStyle.SHORT
        )
    }
    Row (modifier = Modifier
        .padding(vertical = 2.dp)
    ){
        Text(
            text = "${label}: "
        )
        Text(
            text = format.format(count)
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