package com.mycom.chan.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.response.BaseResponse
import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T, R> handleResponse(
        apiCall: suspend () -> Response<BaseResponse<T>>,
        transform: (T) -> R
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
                        message = body.message
                    )
                }
            }    else {
                    val errorResponse = parseError(response)
                    Result.Error(
                        exception = Exception("HTTP ${response.code()}"),
                        message = errorResponse?.message ?: "Connection Error: ${response.code()}"
                    )
                }
            }  catch (e: Exception) {
            Log.e("BaseRepository", "Network Crash", e)
            Result.Error(e, "Connection Error: Unknown error")
        }
    }
    private fun <T> parseError(response: Response<BaseResponse<T>>): BaseResponse<T>? {
        return try {
            val errorJson = response.errorBody()?.string()
            Gson().fromJson(errorJson, object : TypeToken<BaseResponse<T>>() {}.type)
        } catch (e: Exception) {
            Log.e("BaseRepository", "Parse Error", e)
            null
        }
    }
}