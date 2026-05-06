package com.eji14.cattycat.core.coroutine

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

internal class CoroutineManager(
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) {
    private val jobs = ConcurrentHashMap<String, Job>()

    fun launch(
        key: String = "default",
        onStart: () -> Unit = {},
        onEnd: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ): Boolean {
        if (isRunning(key)) return false

        val job = scope.launch {
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

    fun isRunning(key: String = "default") = jobs[key]?.isActive == true

    fun cancel(key: String = "default"): Boolean {
        val job = jobs[key]
        return if (job?.isActive == true) {
            job.cancel()
            jobs.remove(key)
            true
        } else false
    }

    fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}