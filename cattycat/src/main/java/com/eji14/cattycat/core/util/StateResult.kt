package com.eji14.cattycat.core.util

@Suppress("unused")
sealed class StateResult<out T> {
    data class Success<out T>(val data: T) : StateResult<T>()
    data class Failure(val message: String, val cause: Throwable? = null) : StateResult<Nothing>()
    data class Error(val code: Int, val message: String) : StateResult<Nothing>()
    data object Loading : StateResult<Nothing>()
}