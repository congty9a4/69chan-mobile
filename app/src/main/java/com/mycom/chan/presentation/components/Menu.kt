package com.mycom.chan.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.mycom.chan.R
import com.mycom.chan.domain.model.User
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.viewmodel.AuthState

@Composable
fun Menu(
    authState: AuthState,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Spacing.md)
    ) {
        // Header with the current user
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xl, vertical = Spacing.md)
        ) {
            val currentUser = authState.currentUser
            when {
                authState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(IconSize.medium)
                        .align(Alignment.Center)
                )

                authState.isLoggedIn && currentUser != null -> MenuHeader(
                    user = currentUser,
                    onNavigateToProfile = onNavigateToProfile
                )

                else -> Text(
                    text = "Please log in",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.sm))

        MenuNavItem(R.drawable.message, "Messages", onClick = onNavigateToMessages)
        MenuNavItem(R.drawable.notification, "Notifications", onClick = onNavigateToNotifications)
        // No screens yet for these three: labelled "Soon" so they do not look broken
        MenuNavItem(R.drawable.saved, "Saved posts", comingSoon = true)
        MenuNavItem(R.drawable.setting, "Settings", comingSoon = true)
        MenuNavItem(R.drawable.customer_service, "Customer service", comingSoon = true)

        Spacer(Modifier.weight(1f))

        if (authState.isLoggedIn) {
            HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.sm))
            MenuNavItem(
                iconRes = R.drawable.logout,
                label = "Log out",
                onClick = onLogout
            )
        }
    }
}

@Composable
private fun MenuHeader(
    user: User,
    onNavigateToProfile: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        Avatar(
            imageUrl = user.avatarUrl,
            modifier = Modifier.size(AvatarSize.large),
            contentDescription = "Your avatar",
            onClickLabel = "Open profile",
            onClick = onNavigateToProfile
        )
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Text(
                text = user.fullName.ifBlank { user.userName },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "@${user.userName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MenuNavItem(
    iconRes: Int,
    label: String,
    comingSoon: Boolean = false,
    onClick: () -> Unit = {}
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(IconSize.medium)
            )
        },
        badge = if (comingSoon) {
            {
                Text(
                    text = "Soon",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null,
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
