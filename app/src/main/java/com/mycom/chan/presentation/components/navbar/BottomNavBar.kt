package com.mycom.chan.presentation.components.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycom.chan.R

@Composable
fun BottomNavBar (
    unreadNotificationCount: Long = 0L,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(84.dp)
        .absolutePadding(left = 2.dp, top = 2.dp, right = 2.dp, bottom = 10.dp)
        .clickable(onClick = {}),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically) {

        NavigationButton(R.drawable.home,"home", size = 25, onClick = {})
        NavigationButton(R.drawable.search,"search", size = 25, onClick = {})
        BadgedBox(
            badge = {
                if (unreadNotificationCount > 0L) {
                    Badge {
                        Text(
                            text = if (unreadNotificationCount > 99L) "99+" else unreadNotificationCount.toString(),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        ) {
            IconButton(
                onClick = onNavigateToNotifications,
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.notification),
                    contentDescription = "notification",
                    modifier = Modifier.size(25.dp)
                )
            }
        }
        NavigationButton(R.drawable.message,"message", size = 25, onClick = {})
    }
}

