package com.eji14.cattycat.util

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(
        val message: String,
        val exception: Throwable? = null,
        val code: Int? = null
    ) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Idle : Resource<Nothing>()

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isIdle: Boolean get() = this is Idle

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception ?: Exception(message)
        is Loading -> throw IllegalStateException("Resource is still loading")
        is Idle -> throw IllegalStateException("Resource is idle")
    }

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (String, Throwable?) -> Unit): Resource<T> {
        if (this is Error) action(message, exception)
        return this
    }

    inline fun onLoading(action: () -> Unit): Resource<T> {
        if (this is Loading) action()
        return this
    }

    inline fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, exception, code)
        is Loading -> Loading
        is Idle -> Idle
    }
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: Exception) {
        Resource.Error(
            message = e.message ?: "Unknown error occurred",
            exception = e
        )
    }
}