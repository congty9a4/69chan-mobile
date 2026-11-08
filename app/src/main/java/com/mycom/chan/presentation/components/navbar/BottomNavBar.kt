package com.mycom.chan.presentation.components.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.mycom.chan.R
import com.mycom.chan.presentation.theme.IconSize

enum class BottomNavDestination { HOME, SEARCH, NOTIFICATIONS, MESSAGES }

/**
 * Main tab bar. Material 3 NavigationBar gives 48dp+ touch targets, labels under the icons,
 * a selected indicator, and handles the system navigation bar inset by itself.
 */
@Composable
fun BottomNavBar(
    selected: BottomNavDestination = BottomNavDestination.HOME,
    unreadNotificationCount: Long = 0L,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
) {
    NavigationBar {
        NavItem(
            iconRes = R.drawable.home,
            label = "Home",
            selected = selected == BottomNavDestination.HOME,
            onClick = onNavigateToHome
        )
        NavItem(
            iconRes = R.drawable.search,
            label = "Search",
            selected = selected == BottomNavDestination.SEARCH,
            onClick = onNavigateToSearch
        )
        NavigationBarItem(
            selected = selected == BottomNavDestination.NOTIFICATIONS,
            onClick = onNavigateToNotifications,
            label = { Text("Notifications") },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0L) {
                            val countText =
                                if (unreadNotificationCount > 99L) "99+" else unreadNotificationCount.toString()
                            Badge(
                                modifier = Modifier.semantics {
                                    contentDescription = "$unreadNotificationCount unread"
                                }
                            ) { Text(countText) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.notification),
                        contentDescription = null,
                        modifier = Modifier.size(IconSize.medium)
                    )
                }
            }
        )
        NavItem(
            iconRes = R.drawable.message,
            label = "Messages",
            selected = selected == BottomNavDestination.MESSAGES,
            onClick = onNavigateToMessages
        )
    }
}

@Composable
private fun RowScope.NavItem(
    iconRes: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        icon = {
            // The visible label already names the tab, so the icon itself stays silent
            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(IconSize.medium)
            )
        }
    )
}
