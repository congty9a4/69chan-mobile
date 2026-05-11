package com.mycom.chan.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.response.BaseResponse
import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T, R> handleResponse(
        apiCall: suspend () -> Response<BaseResponse<T>>,
        transform: suspend (T) -> R
    ) : Result<R> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body!=null) {
                if (body.success && body.body != null) {
                    Result.Success(transform(body.body))
                } else {
                    Result.Error(
                        exception = Exception("API_ERROR"),
                        message = body.message,
                        errorCode = body.errorCode
                    )
                }
            }    else {
                    val errorResponse = parseError(response)
                    Result.Error(
                        exception = Exception("HTTP ${response.code()}"),
                        message = errorResponse?.message ?: "Connection Error: ${response.code()}",
                        errorCode = errorResponse?.errorCode
                    )
                }
            }  catch (e: Exception) {
            Log.e("BaseRepository", "Network Crash", e)
            Result.Error(e, "Connection Error: Unknown error")
        }
    }
    protected suspend fun handleUnitResponse(
        apiCall: suspend () -> Response<BaseResponse<Unit>>
    ): Result<Unit> {
        return try {
            val response = apiCall()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                if (body.success) Result.Success(Unit)
                else Result.Error(Exception("API_ERROR"), body.message, body.errorCode)
            } else {
                val errorResponse = parseError(response)
                Result.Error(
                    Exception("HTTP ${response.code()}"),
                    errorResponse?.message ?: "Connection Error: ${response.code()}",
                    errorResponse?.errorCode
                )
            }
        } catch (e: Exception) {
            Log.e("BaseRepository", "Network Crash", e)
            Result.Error(e, "Connection Error: Unknown error")
        }
    }

    private fun <T> parseError(response: Response<BaseResponse<T>>): BaseResponse<T>? {
        return try {
            val errorJson = response.errorBody()?.string() ?: return null
            val json = Gson().fromJson(errorJson, JsonObject::class.java)
            val message = json.get("message")?.asString ?: "Unknown error"
            val detail = json.get("detail")?.asString
            val fullMessage = if (!detail.isNullOrBlank()) "$message\n$detail" else message
            BaseResponse(
                body = null,
                success = json.get("success")?.asBoolean ?: false,
                timestamp = json.get("timestamp")?.asString ?: "",
                message = fullMessage,
                errorCode = json.get("status")?.asInt ?: response.code()
            )
        } catch (e: Exception) {
            Log.e("BaseRepository", "Parse Error", e)
            null
        }
    }
}