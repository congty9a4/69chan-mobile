package com.mycom.chan.presentation.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycom.chan.presentation.components.BirthdayPickerField
import com.mycom.chan.presentation.components.login_register.AuthHeader
import com.mycom.chan.presentation.components.login_register.AuthPrimaryButton
import com.mycom.chan.presentation.components.login_register.AuthScreenContainer
import com.mycom.chan.presentation.theme.Spacing
import com.mycom.chan.presentation.viewmodel.ProfileEvent
import com.mycom.chan.presentation.viewmodel.ProfileViewModel

@Composable
fun CreateProfileScreen(
    userId: String,
    viewModel: ProfileViewModel = hiltViewModel(),
    onProfileCreated: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                ProfileEvent.CreateSuccess -> onProfileCreated()
                ProfileEvent.UpdateSuccess -> {}
                is ProfileEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    CreateProfileContent(
        isSaving = uiState.isSaving,
        snackbarHostState = snackbarHostState,
        onSubmit = { fullname, bio, birthday, phone ->
            viewModel.createProfile(
                userId = userId,
                fullname = fullname,
                bio = bio,
                birthday = birthday,
                phone = phone
            )
        },
        onBackToLogin = onBackToLogin
    )
}

/** Profile setup form without the ViewModel, so the debug preview screen can show it. */
@Composable
internal fun CreateProfileContent(
    isSaving: Boolean,
    snackbarHostState: SnackbarHostState,
    onSubmit: (fullname: String, bio: String, birthday: String, phone: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    // rememberSaveable: what the user typed survives rotation
    var fullname by rememberSaveable { mutableStateOf("") }
    var bio by rememberSaveable { mutableStateOf("") }
    var birthday by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    val submit = { onSubmit(fullname, bio, birthday, phone) }

    AuthScreenContainer(snackbarHostState = snackbarHostState) {
        AuthHeader(
            title = "Set up your profile",
            subtitle = "Tell others a bit about yourself",
            showLogo = false
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            OutlinedTextField(
                value = fullname,
                onValueChange = { fullname = it },
                label = { Text("Display name") },
                supportingText = { Text("Required") },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.PersonFullName }
            )
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                maxLines = 3,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )
            BirthdayPickerField(
                value = birthday,
                onValueChange = { birthday = it }
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (!isSaving && fullname.isNotBlank()) submit()
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.PhoneNumber }
            )
        }
        AuthPrimaryButton(
            text = "Create profile",
            onClick = submit,
            enabled = fullname.isNotBlank(),
            isLoading = isSaving
        )
        TextButton(onClick = onBackToLogin) {
            Text("Back to login")
        }
    }
}
