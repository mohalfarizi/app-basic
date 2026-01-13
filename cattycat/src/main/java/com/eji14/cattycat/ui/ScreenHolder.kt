package com.eji14.cattycat.ui

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eji14.cattycat.coroutine.CoroutineManager
import kotlinx.coroutines.CoroutineScope
import java.io.Closeable

abstract class ScreenHolder(
    protected val coroutine: CoroutineManager = CoroutineManager(),
    val refreshable: Boolean = false,
    val autoRefresh: Boolean = false,
    val refreshOnStart: Boolean = false,
    val keepOnBack: Boolean = true,
    val enableNotification: Boolean = false,
    val secondsBeforeRefresh: Long = 120
) : Closeable {
    var lastUsedTime: Long = System.currentTimeMillis()
    var lastRefreshedTime: Long = 0L
    var showNetworkErrorDialog by mutableStateOf(false)
        private set
    var notification: NotificationState? by mutableStateOf(null)
        private set
    @OptIn(ExperimentalMaterial3Api::class)
    val pullToRefreshState: PullToRefreshState? = if (refreshable) PullToRefreshState() else null
    var isRefreshing by mutableStateOf(false)
        private set

    private var hasStarted = false

    protected val scope: CoroutineScope
        get() = coroutine.scope

    protected fun launch(
        key: String = "",
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {},
        showRefresh: Boolean = false,
        content: suspend CoroutineScope.() -> Unit
    ) {
        coroutine.launch(
            key = key,
            onStart = {
                if (showRefresh) isRefreshing = true
                onStart()
            },
            onEnd = {
                if (showRefresh) isRefreshing = false
                onEnd()
            },
            block = content
        )
    }

    /**
     * will be called on every screen launchedEffect
     */
    fun launchEffect() {
        launch(showRefresh = true) {
            log("launch: called")
            if (!hasStarted) {
                log("onStart: started")
                onStart()
                hasStarted = true
                log("onStart: finished")
            }

            val condition1 = refreshable && refreshOnStart && lastRefreshedTime < System.currentTimeMillis() - secondsBeforeRefresh * 1000
            val condition2 = refreshable && autoRefresh
            if (condition1 || condition2) {
                log("onRefresh-launch: started")
                onRefresh()
                lastRefreshedTime = System.currentTimeMillis()
                log("onRefresh-launch: finished")
            }
        }
    }

    fun refresh() {
        launch(showRefresh = true) {
            log("onRefresh: started")
            onRefresh()
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

    protected fun notif(
        message: String,
        type: NotifType = NotifType.INFO,
        duration: Long = 3000L,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        if (enableNotification) notification = NotificationState(
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

    override fun close() {
        onCleared()
    }
}

data class NotificationState(
    val message: String,
    val type: NotifType,
    val duration: Long,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val onDismiss: () -> Unit
)

enum class NotifType {
    SUCCESS, ERROR, WARNING, INFO
}