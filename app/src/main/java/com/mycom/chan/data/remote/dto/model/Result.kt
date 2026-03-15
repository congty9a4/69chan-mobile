package com.mycom.chan.data.remote.dto.model

sealed  class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Exception, val message: String = "Unknown error") :
        Result<Nothing>()
}