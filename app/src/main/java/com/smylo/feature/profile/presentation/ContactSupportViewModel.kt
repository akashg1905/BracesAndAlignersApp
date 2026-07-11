package com.smylo.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smylo.BuildConfig
import com.smylo.core.network.dto.SupportTopic
import com.smylo.feature.profile.data.SupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ContactSupportUiState {
    object Idle : ContactSupportUiState()
    object Loading : ContactSupportUiState()
    data class Success(val ticketId: String, val detail: String) : ContactSupportUiState()
    data class Error(val message: String) : ContactSupportUiState()
}

@HiltViewModel
class ContactSupportViewModel @Inject constructor(
    private val supportRepository: SupportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ContactSupportUiState>(ContactSupportUiState.Idle)
    val uiState: StateFlow<ContactSupportUiState> = _uiState.asStateFlow()

    private val _topics = MutableStateFlow<List<SupportTopic>>(emptyList())
    val topics: StateFlow<List<SupportTopic>> = _topics.asStateFlow()

    private val _isFetchingTopics = MutableStateFlow(false)
    val isFetchingTopics: StateFlow<Boolean> = _isFetchingTopics.asStateFlow()

    init {
        fetchTopics()
    }

    fun fetchTopics() {
        viewModelScope.launch {
            _isFetchingTopics.value = true
            try {
                val topicsList = supportRepository.getSupportTopics()
                _topics.value = topicsList
            } catch (e: Exception) {
                // Could handle error state for topics specifically if needed
            } finally {
                _isFetchingTopics.value = false
            }
        }
    }

    fun contactSupport(topicId: String, message: String) {
        if (topicId.isBlank() || message.isBlank()) {
            _uiState.value = ContactSupportUiState.Error("Please fill in all fields")
            return
        }

        viewModelScope.launch {
            _uiState.value = ContactSupportUiState.Loading
            try {
                val response = supportRepository.createSupportQuery(
                    topic = topicId,
                    message = message,
                    appVersion = BuildConfig.VERSION_NAME
                )
                _uiState.value = ContactSupportUiState.Success(
                    ticketId = response.ticketId,
                    detail = response.detail
                )
            } catch (e: Exception) {
                _uiState.value = ContactSupportUiState.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    fun resetState() {
        _uiState.value = ContactSupportUiState.Idle
    }
}
