package com.mycom.chan.presentation.page

import com.mycom.chan.presentation.components.BackButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mycom.chan.R
import com.mycom.chan.presentation.components.Avatar
import com.mycom.chan.presentation.components.BirthdayPickerField
import com.mycom.chan.presentation.theme.AvatarSize
import com.mycom.chan.presentation.theme.IconSize
import com.mycom.chan.presentation.theme.MaxFormWidth
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.viewmodel.ProfileEvent
import com.mycom.chan.presentation.viewmodel.ProfileState
import com.mycom.chan.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    userId: String,
    isOwnProfile: Boolean,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userId) { viewModel.loadProfile(userId) }

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                ProfileEvent.UpdateSuccess -> snackbarHostState.showSnackbar("Profile saved")
                ProfileEvent.CreateSuccess -> snackbarHostState.showSnackbar("Profile created")
                // Errors were silently dropped before; the user now sees what went wrong
                is ProfileEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    ProfileContent(
        uiState = uiState,
        isOwnProfile = isOwnProfile,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onStartEditing = viewModel::startEditing,
        onFollow = { viewModel.toggleFollow(userId) },
        onBioChange = viewModel::onBioChange,
        onLocationChange = viewModel::onLocationChange,
        onBirthdayChange = viewModel::onBirthdayChange,
        onPhoneChange = viewModel::onPhoneChange,
        onCancelEditing = viewModel::cancelEditing,
        onSave = {
            if (uiState.profile == null) viewModel.createProfile(
                userId = userId,
                fullname = "",
                bio = uiState.bio,
                birthday = uiState.birthday,
                phone = uiState.phone
            )
            else viewModel.updateProfile(userId)
        }
    )
}

/** Profile UI (view and edit modes) without the ViewModel, so the debug preview screen can show it with sample data. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileContent(
    uiState: ProfileState,
    isOwnProfile: Boolean,
    onBack: () -> Unit,
    onStartEditing: () -> Unit,
    onFollow: () -> Unit,
    onBioChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onBirthdayChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCancelEditing: () -> Unit,
    onSave: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(uiState.profile?.user?.userName ?: "Profile") },
                navigationIcon = {
                    BackButton(onClick = onBack)
                },
                actions = {
                    if (isOwnProfile && !uiState.isEditing) {
                        IconButton(onClick = onStartEditing) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.edit),
                                contentDescription = "Edit profile",
                                modifier = Modifier.size(IconSize.medium)
                            )
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
                onBioChange = onBioChange,
                onLocationChange = onLocationChange,
                onBirthdayChange = onBirthdayChange,
                onPhoneChange = onPhoneChange,
                onCancel = onCancelEditing,
                onSave = onSave,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            ViewProfileContent(
                uiState = uiState,
                onFollow = onFollow,
                isOwnProfile = isOwnProfile,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

private val CoverHeight = 160.dp

@Composable
private fun ViewProfileContent(
    uiState: ProfileState,
    isOwnProfile: Boolean,
    onFollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile = uiState.profile
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Box {
            // Cover photo, or a neutral block when there is none
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CoverHeight)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
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
            // Avatar overlaps the bottom of the cover, ringed in the page color
            Avatar(
                imageUrl = profile?.avatarUrl,
                modifier = Modifier
                    .padding(start = Spacing.lg)
                    .offset(y = CoverHeight - AvatarSize.profile / 2)
                    .size(AvatarSize.profile)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
        Spacer(Modifier.height(AvatarSize.profile / 2 + Spacing.md))

        Column(
            modifier = Modifier.padding(horizontal = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            Text(
                text = profile?.user?.fullName.orEmpty(),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "@${profile?.user?.userName.orEmpty()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!profile?.bio.isNullOrBlank()) {
                Spacer(Modifier.height(Spacing.xs))
                Text(profile.bio, style = MaterialTheme.typography.bodyLarge)
            }
            if (!profile?.location.isNullOrBlank()) {
                Text(
                    text = profile.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!isOwnProfile) {
                Spacer(Modifier.height(Spacing.md))
                // Following looks different from Follow, not only by its label
                if (uiState.isFollowing) {
                    OutlinedButton(
                        onClick = onFollow,
                        enabled = !uiState.isFollowLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Following") }
                } else {
                    Button(
                        onClick = onFollow,
                        enabled = !uiState.isFollowLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Follow") }
                }
            }
        }
        Spacer(Modifier.height(Spacing.xl))
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
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = MaxFormWidth)
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            val fieldShape = MaterialTheme.shapes.medium
            OutlinedTextField(
                value = uiState.bio,
                onValueChange = onBioChange,
                label = { Text("Bio") },
                maxLines = 4,
                shape = fieldShape,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.location,
                onValueChange = onLocationChange,
                label = { Text("Location") },
                singleLine = true,
                shape = fieldShape,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth()
            )
            BirthdayPickerField(value = uiState.birthday, onValueChange = onBirthdayChange)
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = onPhoneChange,
                label = { Text("Phone") },
                singleLine = true,
                shape = fieldShape,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.padding(top = Spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) { Text("Cancel") }
                Button(onClick = onSave, modifier = Modifier.weight(1f), enabled = !uiState.isSaving) {
                    Text(if (uiState.profile == null) "Create" else "Save")
                }
            }
        }
    }
}
