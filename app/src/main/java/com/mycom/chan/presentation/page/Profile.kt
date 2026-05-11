package com.mycom.chan.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import com.mycom.chan.R
import com.mycom.chan.presentation.components.BirthdayPickerField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mycom.chan.domain.model.User
import com.mycom.chan.presentation.viewmodel.ProfileEvent
import com.mycom.chan.presentation.viewmodel.ProfileViewModel
import com.mycom.chan.domain.model.Profile
import com.mycom.chan.presentation.theme.ChanTheme
import com.mycom.chan.presentation.viewmodel.ProfileState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    userId: String,
    isOwnProfile: Boolean,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) { viewModel.loadProfile(userId) }

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                ProfileEvent.UpdateSuccess, ProfileEvent.CreateSuccess -> {}
                is ProfileEvent.ShowError -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.profile?.user?.userName ?: "Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.back),
                            contentDescription = null)
                    }
                },
                actions = {
                    if (isOwnProfile && !uiState.isEditing) {
                        IconButton(onClick = { viewModel.startEditing() }) {
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.edit), contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (uiState.isEditing) {
            EditProfileContent(
                uiState = uiState,
                onBioChange = viewModel::onBioChange,
                onLocationChange = viewModel::onLocationChange,
                onBirthdayChange = viewModel::onBirthdayChange,
                onPhoneChange = viewModel::onPhoneChange,
                onCancel = viewModel::cancelEditing,
                onSave = {
                    if (uiState.profile == null) viewModel.createProfile(
                        userId = userId,
                        fullname = "",
                        bio = uiState.bio,
                        birthday = uiState.birthday,
                        phone = uiState.phone
                    )
                    else viewModel.updateProfile(userId)
                },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            ViewProfileContent(
                uiState = uiState,
                onFollow = { viewModel.toggleFollow(userId) },
                isOwnProfile = isOwnProfile,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ViewProfileContent(
    uiState: ProfileState,
    isOwnProfile: Boolean,
    onFollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = uiState.profile
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        // Cover photo
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp)
                .background(Color(0xFFE0E0E0))
        ) {
            if (!profile?.coverPhotoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = profile.coverPhotoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Avatar + info
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = profile?.avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(CircleShape)
                    .background(Color(0xFFBDBDBD))
            )
            Spacer(Modifier.height(8.dp))
            Text(profile?.user?.fullName ?: "", style = MaterialTheme.typography.titleLarge)
            Text("@${profile?.user?.userName ?: ""}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            if (!profile?.bio.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(profile.bio, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(12.dp))
            if (!isOwnProfile) {
                Button(
                    onClick = onFollow,
                    enabled = !uiState.isFollowLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (uiState.isFollowing) "Following" else "Follow")
                }
            }
            if (!profile?.location.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(profile.location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun EditProfileContent(
    uiState: ProfileState,
    onBioChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onBirthdayChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = uiState.bio, onValueChange = onBioChange, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = uiState.location, onValueChange = onLocationChange, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
        BirthdayPickerField(value = uiState.birthday, onValueChange = onBirthdayChange)
        OutlinedTextField(value = uiState.phone, onValueChange = onPhoneChange, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) { Text("Cancel") }
            Button(onClick = onSave, modifier = Modifier.weight(1f), enabled = !uiState.isLoading) {
                Text(if (uiState.profile == null) "Create" else "Save")
            }
        }
    }
}
