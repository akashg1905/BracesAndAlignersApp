package com.smylo.core.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

data class ErrorToastEvent(
    val message: String,
    val id: Long = System.currentTimeMillis()
)

@Singleton
class ErrorMessenger @Inject constructor() {
    private val _events = MutableSharedFlow<ErrorToastEvent>(extraBufferCapacity = 8)
    val events: SharedFlow<ErrorToastEvent> = _events.asSharedFlow()

    fun show(message: String) {
        _events.tryEmit(ErrorToastEvent(message = message))
    }
}
