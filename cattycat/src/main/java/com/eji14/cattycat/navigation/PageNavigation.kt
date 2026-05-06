package com.eji14.cattycat.navigation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.eji14.cattycat.ui.screen.ScreenHolder
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused")
class PageNavigation<P : PageNavigation.Page>(
    private val homePage: P,
    private val enableLogging: Boolean = true
) : Closeable {

    @Suppress("unused")
    enum class Holder(val maxInstances: Int) {
        SINGLE(1),
        LIMITED(5),
        DEFAULT(10),
    }

    enum class AnimationStyle {
        SLIDE,
        FADE_SCALE,
}

    abstract class Page(
        val identifier: String,
        val routeKey: String = identifier,
        val holder: Holder = Holder.DEFAULT,
        val animationStyle: AnimationStyle = AnimationStyle.SLIDE
    ) {
        open fun getRouteData(): Map<String, Any?> = emptyMap()
    }

    private data class NavigationEntry<P : Page>(
        val page: P,
        val additionalData: Map<String, Any?> = emptyMap(),
        val timestamp: Long = System.currentTimeMillis()
    )

    data class NavigationState<P : Page>(
        val currentPage: P,
        val canGoBack: Boolean,
        val stackSize: Int
    )

    enum class NavDirection {
        FORWARD, BACKWARD, REPLACE, NONE
    }

    private val navigationStack = ArrayDeque<NavigationEntry<P>>()

    private val holdersMap = ConcurrentHashMap<Holder, MutableMap<String, ScreenHolder>>()

    var currentState by mutableStateOf(NavigationState(homePage, false, 1))
        private set

    var navDirection by mutableStateOf(NavDirection.NONE)
        private set

    var currentData: Map<String, Any?> = emptyMap()
        private set

    @Volatile
    private var isNavigating = false

    companion object {
        private const val TAG = "PageNavigation"
        private const val HOLDER_MAX_AGE_MS = 5 * 60 * 1000L
        const val NAVIGATION_ANIMATION_DURATION = 300
    }

    init {
        navigationStack.addLast(NavigationEntry(homePage))
        currentData = buildDataMap(homePage, emptyMap())
    }

    fun navigateTo(page: P, additionalData: Map<String, Any?> = emptyMap()) {
        synchronized(this) {
            if (isNavigating) {
                logDebug("Navigation blocked: already navigating")
                return
            } else if (page == navigationStack.last().page) {
                logDebug("Navigation blocked: already on the page")
                return
            }

            performNavigation {
                navDirection = NavDirection.FORWARD
                navigationStack.addLast(NavigationEntry(page, additionalData))
                finalizeNavigation(page, additionalData)
            }
        }
    }

    fun replace(
        page: P,
        additionalData: Map<String, Any?> = emptyMap(),
        direction: NavDirection = NavDirection.REPLACE
    ) {
        require(direction != NavDirection.NONE) { "replace() direction must not be NONE; use REPLACE, FORWARD, or BACKWARD" }
        synchronized(this) {
            if (isNavigating) {
                logDebug("Replace blocked: already navigating")
                return
            } else if (page == navigationStack.last().page) {
                logDebug("Navigation blocked: already on the page")
                return
            }

            performNavigation {
                navDirection = direction

                if (navigationStack.isNotEmpty()) {
                    val replaced = navigationStack.removeLast()
                    cleanupHolder(replaced.page)
                }

                navigationStack.addLast(NavigationEntry(page, additionalData))
                finalizeNavigation(page, additionalData)
            }
        }
    }

    fun back(resultData: Map<String, Any?>? = null, skipUntil: (P) -> Boolean = { false }) {
        synchronized(this) {
            if (isNavigating || navigationStack.size <= 1) {
                logDebug("Back blocked: navigating or at root")
                return
            }

            performNavigation {
                navDirection = NavDirection.BACKWARD

                val currentEntry = navigationStack.removeLast()
                cleanupHolder(currentEntry.page)

                while (navigationStack.size > 1 && skipUntil(navigationStack.last().page)) {
                    val skippedEntry = navigationStack.removeLast()
                    cleanupHolder(skippedEntry.page)
                    logDebug("Skipped page: ${skippedEntry.page}")
                }

                if (navigationStack.isEmpty()) {
                    navigationStack.addLast(NavigationEntry(homePage))
                    logDebug("Navigation stack became empty, restored home page")
                }

                val previousEntry = navigationStack.last()

                val mergedAdditionalData = if (resultData != null) {
                    previousEntry.additionalData + resultData
                } else {
                    previousEntry.additionalData
                }

                navigationStack[navigationStack.size - 1] = previousEntry.copy(
                    additionalData = mergedAdditionalData,
                    timestamp = System.currentTimeMillis()
                )

                finalizeNavigation(previousEntry.page, mergedAdditionalData)
            }
        }
    }

    fun backToRoute(routeKey: String, resultData: Map<String, Any?>? = null) {
        back(resultData) { page -> page.routeKey != routeKey }
    }

    fun home(additionalData: Map<String, Any?>? = null) {
        synchronized(this) {
            if (isNavigating) {
                logDebug("Home blocked: already navigating")
                return
            }

            performNavigation {
                clearAllHolders()
                navigationStack.clear()

                val finalData = additionalData ?: emptyMap()
                navigationStack.addLast(NavigationEntry(homePage, finalData))
                navDirection = NavDirection.NONE
                finalizeNavigation(homePage, finalData)
            }
        }
    }

    internal fun unlockNavigation() {
        isNavigating = false
        clearStaleHolders()
        logDebug("Navigation unlocked")
    }

    private fun performNavigation(block: () -> Unit) {
        isNavigating = true
        block()
        logDebug("Stack: ${navigationStack.map { it.page.identifier }}")
    }

    private fun finalizeNavigation(page: P, additionalData: Map<String, Any?>) {
        currentData = buildDataMap(page, additionalData)
        currentState = NavigationState(
            currentPage = page,
            canGoBack = navigationStack.size > 1,
            stackSize = navigationStack.size
        )
        logDebug("Navigated to: ${page.identifier}, canGoBack: ${navigationStack.size > 1}")
    }

    private fun buildDataMap(page: P, additionalData: Map<String, Any?>): Map<String, Any?> {
        return page.getRouteData() + additionalData
    }

    inline fun <reified T : Page> getCurrentPage(): T? {
        return currentState.currentPage as? T
    }

    inline fun <reified T> getData(key: String): T? {
        return currentData[key] as? T
    }

    inline fun <reified T> getData(key: String, default: T): T {
        return getData(key) ?: default
    }

    fun hasData(key: String): Boolean = currentData.containsKey(key)


    fun updateCurrentData(updates: Map<String, Any?>) {
        synchronized(this) {
            if (navigationStack.isEmpty()) return

            val current = navigationStack.removeLast()
            val newAdditionalData = current.additionalData + updates
            navigationStack.addLast(
                current.copy(
                    additionalData = newAdditionalData,
                    timestamp = System.currentTimeMillis()
                )
            )

            currentData = buildDataMap(current.page, newAdditionalData)
            logDebug("Updated current page data: $updates")
        }
    }


    @Suppress("UNCHECKED_CAST")
    fun <T : ScreenHolder> getHolder(page: P, factory: () -> T): T {
        val type = page.holder
        val holders = holdersMap.getOrPut(type) { mutableMapOf() }

        synchronized(holders) {
            val existing = holders[page.identifier]

            return if (existing != null) {
                logDebug("Reusing holder for: ${page.identifier}")
                existing.lastUsedTime = System.currentTimeMillis()
                existing as T
            } else {
                logDebug("Creating new holder for: ${page.identifier}")

                if (holders.size >= type.maxInstances) {
                    evictLeastRecentlyUsed(holders)
                }

                factory().also {
                    holders[page.identifier] = it
                }
            }
        }
    }

    fun clearHolder(page: P) {
        val holders = holdersMap[page.holder] ?: return
        synchronized(holders) {
            holders.remove(page.identifier)?.let {
                it.onCleared()
                logDebug("Manually cleared holder: ${page.identifier}")
            }
        }
    }

    fun clearHoldersByRoute(routeKey: String) {
        var clearedCount = 0
        holdersMap.values.forEach { holders ->
            synchronized(holders) {
                val toRemove = holders.filter { (identifier, _) ->
                    identifier.startsWith("$routeKey-")
                }
                toRemove.forEach { (identifier, holder) ->
                    holders.remove(identifier)
                    holder.onCleared()
                    clearedCount++
                }
            }
        }
        if (clearedCount > 0) {
            logDebug("Cleared $clearedCount holders for route: $routeKey")
        }
    }

    fun clearStaleHolders() {
        val now = System.currentTimeMillis()
        var clearedCount = 0

        holdersMap.values.forEach { holders ->
            synchronized(holders) {
                val stale = holders.filter { (_, holder) ->
                    !holder.cacheable && (now - holder.lastUsedTime) > HOLDER_MAX_AGE_MS
                }

                stale.forEach { (identifier, holder) ->
                    holders.remove(identifier)
                    holder.onCleared()
                    clearedCount++
                }
            }
        }

        if (clearedCount > 0) {
            logDebug("Cleared $clearedCount stale holders")
        }
    }

    private fun evictLeastRecentlyUsed(holders: MutableMap<String, ScreenHolder>) {
        val lruEntry = holders.entries.minByOrNull { it.value.lastUsedTime }
        if (lruEntry != null) {
            logDebug("Evicting LRU holder: ${lruEntry.key}")
            holders.remove(lruEntry.key)?.onCleared()
        }
    }

    private fun cleanupHolder(page: P) {
        val holders = holdersMap[page.holder] ?: return
        synchronized(holders) {
            val holder = holders.remove(page.identifier)

            when {
                holder == null -> {}
                holder.cacheable -> {
                    holder.lastUsedTime = System.currentTimeMillis()
                    holders[page.identifier] = holder
                    logDebug("Kept holder (keepOnBack=true): ${page.identifier}")
                }

                else -> {
                    holder.onCleared()
                    logDebug("Cleared holder: ${page.identifier}")
                }
            }
        }
    }

    private fun clearAllHolders() {
        var clearedCount = 0
        holdersMap.values.forEach { holders ->
            synchronized(holders) {
                holders.values.forEach {
                    it.onCleared()
                    clearedCount++
                }
                holders.clear()
            }
        }
        logDebug("Cleared all holders (count: $clearedCount)")
    }

    override fun close() {
        clearAllHolders()
    }

    private fun logDebug(message: String) {
        if (enableLogging) {
            Log.d(TAG, message)
        }
    }
}