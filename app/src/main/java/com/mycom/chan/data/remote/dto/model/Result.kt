package com.mycom.chan.data.remote.dto.model

sealed  class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable, val message: String?) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
