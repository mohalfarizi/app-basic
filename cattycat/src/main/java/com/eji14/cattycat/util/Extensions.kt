package com.eji14.cattycat.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> = collectAsState(context)

fun <T> List<T>.replace(predicate: (T) -> Boolean, replacement: T): List<T> {
    return map { if (predicate(it)) replacement else it }
}

fun <T> List<T>.replaceOrAdd(predicate: (T) -> Boolean, replacement: T): List<T> {
    val found = any(predicate)
    return if (found) {
        map { if (predicate(it)) replacement else it }
    } else {
        this + replacement
    }
}

fun <T> List<T>.updateItem(predicate: (T) -> Boolean, update: (T) -> T): List<T> {
    return map { if (predicate(it)) update(it) else it }
}

fun <K, V> Map<K, V>.update(key: K, value: V): Map<K, V> {
    return this + (key to value)
}

fun String?.orDefault(default: String = ""): String = this ?: default

fun <T> T?.orThrow(message: String = "Value is null"): T {
    return this ?: throw IllegalStateException(message)
}

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T {
    return if (condition) block() else this
}

inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}

inline fun <T, R> T.runCatchingOrNull(block: T.() -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}