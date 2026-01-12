package com.eji14.cattycat.ui

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eji14.cattycat.coroutine.CoroutineManager
import kotlinx.coroutines.delay

abstract class ScreenHolder(
    protected val coroutine: CoroutineManager = CoroutineManager(),
    val refreshable: Boolean = false,
    val autoRefresh: Boolean = false,
    val refreshOnStart: Boolean = false,
    val keepOnBack: Boolean = false
) {
    var lastUsedTime: Long = System.currentTimeMillis()

    var showNetworkErrorDialog by mutableStateOf(false)
        private set

    var notification by mutableStateOf<NotificationState?>(null)
        private set

    @OptIn(ExperimentalMaterial3Api::class)
    var pullToRefreshState: PullToRefreshState? = null
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    private var hasStarted = false

    init {
        if (refreshable) {
            @OptIn(ExperimentalMaterial3Api::class)
            pullToRefreshState = PullToRefreshState()
        }
    }

    /**
     * will be called on every screen launchedEffect
     */
    fun launch() {
        coroutine.launch {
            log("launch: called")
            if (!hasStarted) {
                isRefreshing = true
                log("onStart: started")
                onStart()
                isRefreshing = false
                log("onStart: finished")
            }

            val condition1 = refreshable && refreshOnStart && !hasStarted
            val condition2 = refreshable && autoRefresh
            if (condition1 || condition2) {
                log("onRefresh-launch: started")
                onRefresh()
                log("onRefresh-launch: finished")
            }

            hasStarted = true
        }
    }

    fun refresh() {
        coroutine.launch {
            isRefreshing = true
            log("onRefresh: started")
            onRefresh()
            isRefreshing = false
            log("onRefresh: finished")
        }
    }

    protected open suspend fun onStart() {}
    protected open suspend fun onRefresh() {}

    fun showNetworkError() {
        showNetworkErrorDialog = true
    }

    fun dismissNetworkError() {
        showNetworkErrorDialog = false
    }

    fun showNotification(
        message: String,
        type: NotificationType = NotificationType.INFO,
        duration: Long = 3000L,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        notification = NotificationState(
            message = message,
            type = type,
            duration = duration,
            actionLabel = actionLabel,
            onAction = onAction,
            onDismiss = ::dismissNotification
        )
    }

    fun dismissNotification() {
        notification = null
    }

    open fun onCleared() {
        log("onCleared: called")
        coroutine.cancelAll()
    }

    private fun log(message: String) {
        Log.d("ScreenHolder", message)
    }
}

data class NotificationState(
    val message: String,
    val type: NotificationType,
    val duration: Long,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val onDismiss: () -> Unit
)

enum class NotificationType {
    SUCCESS, ERROR, WARNING, INFO
}