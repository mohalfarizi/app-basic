package com.eji14.cattycat.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eji14.cattycat.core.coroutine.CoroutineManager
import com.eji14.cattycat.test.TodoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.io.Closeable

@Suppress("unused")
abstract class ScreenHolder(
    name: String? = null,
    private val refreshEveryResume: Boolean = false,
    private val refreshDelayDurationSeconds: Long = 10,
    private val refreshOnStart: Boolean = false,
    internal val cacheable: Boolean = true,
    internal val enableManualExit: Boolean = false,
    private val enableNotification: Boolean = false,
    private val maxNotifications: Int = Int.MAX_VALUE,
    saveScrollState: Boolean = true,
    scrollOffset: Int = 0,
    initializeWithLoading: Boolean = false,
    enableDialog: Boolean = false,
    enablePullToRefresh: Boolean = false,
) : Closeable {

    internal val todoState = if (name != null) TodoState(name) else null

    private val coroutine: CoroutineManager = CoroutineManager()
    @OptIn(ExperimentalMaterial3Api::class)
    internal val pullToRefreshState: PullToRefreshState? = if (enablePullToRefresh) PullToRefreshState() else null
    internal val scrollState: ScrollState? = if (saveScrollState) ScrollState(scrollOffset) else null
    internal var onManualExit: () -> Unit = {}
    var showInitializeLoading by mutableStateOf(initializeWithLoading)
    internal var progressState by mutableStateOf<ProgressState>(ProgressState.None)
    internal var lastUsedTime: Long = System.currentTimeMillis()

    internal var isRefreshing by mutableStateOf(false)
        private set

    var notifications by mutableStateOf<List<NotificationState>>(emptyList())
        private set

    private var hasStarted = false
    private var lastRefreshedTime: Long = 0L
    protected val scope: CoroutineScope get() = coroutine.scope
    protected fun <R> Flow<R>.asStateFlow(default: R) =
        stateIn(scope, SharingStarted.WhileSubscribed(5000), default)

    protected fun launch(
        key: String = "default", onStart: () -> Unit = {}, onEnd: () -> Unit = {}, showProgress: Boolean = false, block: suspend CoroutineScope.() -> Unit
    ) {
        coroutine.launch(
            key = key,
            onStart = {
                if (showProgress && !showInitializeLoading) progressState = ProgressState.Indeterminate
                onStart()
            },
            onEnd = {
                if (showProgress) progressState = ProgressState.None
                onEnd()
            },
            block = block,
        )
    }

    protected fun cancel(key: String = "default") = coroutine.cancel(key)

    internal fun launchEffect() {
        launch(showProgress = true) {
            if (!hasStarted) {
                onStart()
                hasStarted = true
            }
            val isStale = lastRefreshedTime < System.currentTimeMillis() - refreshDelayDurationSeconds * 1000
            if (refreshEveryResume || (refreshOnStart && isStale)) {
                onRefresh()
                lastRefreshedTime = System.currentTimeMillis()
            }
        }
    }

    fun refresh() {
        launch(showProgress = pullToRefreshState == null) {
            isRefreshing = true
            onRefresh()
            lastRefreshedTime = System.currentTimeMillis()
            isRefreshing = false
        }
    }

    fun resetState() = onResetState()

    fun exit() {
        if (enableManualExit) onManualExit()
    }

    fun notify(
        id: String = "default",
        message: String,
        type: NotifType = NotifType.INFO,
        duration: Long = 2000L,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
    ) {
        if (!enableNotification) return

        val new = NotificationState(
            id = id,
            message = message,
            type = type,
            duration = duration,
            actionLabel = actionLabel,
            onAction = onAction,
            version = System.currentTimeMillis(),
            onDismiss = { dismissNotification(id) },
        )

        val current = notifications.toMutableList()
        val existingIndex = current.indexOfFirst { it.id == id }

        if (existingIndex != -1) {
            current[existingIndex] = new
        } else {
            if (current.size >= maxNotifications) current.removeLastOrNull()
            current.add(0, new)
        }

        notifications = current
    }

    fun dismissNotification(id: String = "default") {
        notifications = notifications.filter { it.id != id }
    }

    fun dismissAllNotifications() {
        notifications = emptyList()
    }

    protected open suspend fun onStart() {}
    protected open suspend fun onRefresh() {}
    protected open fun onResetState() {}

    open fun onCleared() {
        coroutine.cancelAll()
    }

    override fun close() {
        onCleared()
    }
}

data class NotificationState(
    val id: String,
    val message: String,
    val type: NotifType,
    val duration: Long,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val version: Long = System.currentTimeMillis(),
    val onDismiss: () -> Unit,
)

enum class NotifType { SUCCESS, ERROR, WARNING, INFO }

sealed class ProgressState {
    object None : ProgressState()
    object Indeterminate : ProgressState()
    data class Determinate(val progress: Float) : ProgressState() {
        init {
            require(progress in 0f..1f) { "Progress must be between 0 and 1" }
        }
    }
}