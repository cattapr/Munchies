package com.example.munchies.feature.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Singleton

@Singleton
class SnackbarService(
    private val coroutineScope: CoroutineScope
) {
    val snackbarHostState = SnackbarHostState()
    private val mutex = Mutex()

    fun dismiss() {
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    fun show(
        message: String,
        actionLabel: String? = null,
        onAction: () -> Unit = {}
    ) = coroutineScope.launch {
        mutex.withLock {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = if (actionLabel != null) {
                    SnackbarDuration.Indefinite  // ← stays until user acts
                } else {
                    SnackbarDuration.Short       // ← auto dismisses
                }
            )
            if (result == SnackbarResult.ActionPerformed) {
                onAction()
            }
        }
    }
}