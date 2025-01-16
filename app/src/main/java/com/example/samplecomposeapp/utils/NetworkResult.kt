package com.example.samplecomposeapp.utils

//<out T> - You can read values of type T but cannot modify them.
sealed class NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>()

    //<out T>: This means T is covariant, so the success result can hold any type,
    // or any subtype of T. For example, Success<Int> and Success<String> are both valid.
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}