package com.eji14.cattycat.coroutine

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

class CoroutineManager(
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private val jobs = ConcurrentHashMap<String, Job>()

    fun launch(
        key: String = "default",
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ): Boolean {
        if (isRunning(key)) return false

        val job = scope.launch(dispatcher) {
            try {
                onStart()
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onError(e)
            } finally {
                withContext(NonCancellable) {
                    onEnd()
                    jobs.remove(key)
                }
            }
        }

        jobs[key] = job
        return true
    }

    fun launchIO(
        key: String = "default",
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ): Boolean = launch(key, Dispatchers.IO, onStart, onEnd, onError, block)

    fun isRunning(key: String = "default"): Boolean {
        return jobs[key]?.isActive == true
    }

    fun cancel(key: String = "default"): Boolean {
        val job = jobs[key]
        return if (job?.isActive == true) {
            job.cancel()
            jobs.remove(key)
            true
        } else {
            false
        }
    }

    fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }

    suspend fun await(key: String = "default") {
        jobs[key]?.join()
    }

    fun getActiveCount(): Int = jobs.count { it.value.isActive }

    fun cleanup() {
        jobs.entries.removeIf { !it.value.isActive }
    }

    fun getActiveKeys(): List<String> = jobs.filter { it.value.isActive }.keys.toList()
}