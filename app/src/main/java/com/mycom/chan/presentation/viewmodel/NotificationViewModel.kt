package com.mycom.chan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Notification
import com.mycom.chan.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationState(
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Long = 0L,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())
    val uiState: StateFlow<NotificationState> = _uiState.asStateFlow()

    init {
        loadNotifications()
        loadUnreadCount()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = notificationRepository.getMyNotifications()) {
                is Result.Success -> _uiState.update {
                    it.copy(notifications = result.data, isLoading = false)
                }
                is Result.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun loadUnreadCount() {
        viewModelScope.launch {
            when (val result = notificationRepository.getUnreadCount()) {
                is Result.Success -> _uiState.update { it.copy(unreadCount = result.data) }
                is Result.Error -> {}
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            when (notificationRepository.markAsRead(notificationId)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        val updated = state.notifications.map { n ->
                            if (n.id == notificationId) n.copy(read = true) else n
                        }
                        val newUnread = updated.count { !it.read }.toLong()
                        state.copy(notifications = updated, unreadCount = newUnread)
                    }
                }
                is Result.Error -> {}
            }
        }
    }

    fun markAllAsRead() {
        val unread = _uiState.value.notifications.filter { !it.read }
        unread.forEach { markAsRead(it.id) }
    }
}