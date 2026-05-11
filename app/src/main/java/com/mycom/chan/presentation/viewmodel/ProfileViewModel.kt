package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Profile
import com.mycom.chan.domain.repository.ProfileRepository
import com.mycom.chan.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val isFollowing: Boolean = false,
    val isFollowLoading: Boolean = false,
    val bio: String = "",
    val location: String = "",
    val birthday: String = "",
    val phone: String = ""
)

sealed interface ProfileEvent {
    object UpdateSuccess : ProfileEvent
    object CreateSuccess : ProfileEvent
    data class ShowError(val message: String) : ProfileEvent
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<ProfileEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = profileRepository.getProfileByUserId(userId)) {
                is Result.Success -> {
                    val p = result.data
                    _uiState.update {
                        it.copy(
                            profile = p,
                            isLoading = false,
                            bio = p.bio ?: "",
                            location = p.location ?: "",
                            birthday = p.birthday ?: "",
                            phone = p.phone ?: ""
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(ProfileEvent.ShowError(result.message ?: "Load profile failed"))
                }
            }
        }
    }

    fun startEditing() = _uiState.update { it.copy(isEditing = true) }
    fun cancelEditing() = _uiState.update { it.copy(isEditing = false) }

    fun onBioChange(v: String) = _uiState.update { it.copy(bio = v) }
    fun onLocationChange(v: String) = _uiState.update { it.copy(location = v) }
    fun onBirthdayChange(v: String) = _uiState.update { it.copy(birthday = v) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v) }

    fun updateProfile(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val state = _uiState.value
            when (val result = profileRepository.updateProfile(
                userId = userId,
                bio = state.bio.takeIf { it.isNotBlank() },
                location = state.location.takeIf { it.isNotBlank() },
                birthday = state.birthday.takeIf { it.isNotBlank() },
                phone = state.phone.takeIf { it.isNotBlank() }
            )) {
                is Result.Success -> {
                    _uiState.update { it.copy(profile = result.data, isSaving = false, isEditing = false) }
                    _eventChannel.send(ProfileEvent.UpdateSuccess)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    _eventChannel.send(ProfileEvent.ShowError(result.message ?: "Save failed"))
                }
            }
        }
    }

    fun createProfile(
        userId: String,
        fullname: String,
        bio: String?,
        birthday: String?,
        phone: String?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val result = profileRepository.createProfile(
                userId = userId,
                fullname = fullname.takeIf { it.isNotBlank() },
                bio = bio?.takeIf { it.isNotBlank() },
                birthday = birthday?.takeIf { it.isNotBlank() },
                phone = phone?.takeIf { it.isNotBlank() }
            )) {
                is Result.Success -> {
                    _uiState.update { it.copy(profile = result.data, isSaving = false) }
                    _eventChannel.send(ProfileEvent.CreateSuccess)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isSaving = false) }
                    _eventChannel.send(ProfileEvent.ShowError(result.message ?: "Create profile failed"))
                }
            }
        }
    }

    fun toggleFollow(userId: String) {
        val isFollowing = _uiState.value.isFollowing
        _uiState.update { it.copy(isFollowLoading = true) }
        viewModelScope.launch {
            val result = if (isFollowing) userRepository.unfollowUser(userId)
                         else userRepository.followUser(userId)
            when (result) {
                is Result.Success -> _uiState.update {
                    it.copy(isFollowing = !isFollowing, isFollowLoading = false)
                }
                is Result.Error -> _uiState.update { it.copy(isFollowLoading = false) }
            }
        }
    }
}
