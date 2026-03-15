package com.mycom.chan.data.repository

import com.google.gson.Gson
import com.mycom.chan.data.remote.api.AuthAPI
import com.mycom.chan.data.remote.dto.model.UserDTO
import com.mycom.chan.data.remote.dto.request.LoginRequest
import com.mycom.chan.data.remote.dto.response.AuthResponse
import com.mycom.chan.data.remote.dto.response.BaseResponse
import com.mycom.chan.domain.repository.AuthRepository
import com.mycom.chan.domain.repository.TokenManager
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.request.LogoutRequest
import com.mycom.chan.data.remote.dto.response.ErrorResponse
import kotlin.jvm.java
import com.mycom.chan.data.remote.dto.request.RegisterRequest

class ImpleAuthRepository @Inject constructor(
    private val tokenManager: TokenManager,
    private val authAPI: AuthAPI,
    private val gson: Gson
) : AuthRepository {

    /****/
    private suspend fun handleAuthResponse (
        response: Response<BaseResponse<AuthResponse>>
    ) : Result<Boolean> {
        if (response.isSuccessful) {
            val body = response.body()
            if(body?.success == true && body.body!=null) {
                tokenManager.saveAccessToken(body.body.token)
                tokenManager.saveRefreshToken(body.body.refreshToken)

                val user = gson.toJson(body.body.user)
                tokenManager.saveUser(user)

                return Result.Success(true)
            }
            return Result.Error(Exception("Authentication failed"), "Request Invalid")
        }
        val errorResponse = response.errorBody()?.string()
        return try {
            if(!errorResponse.isNullOrBlank()) {
                val errorObject = gson.fromJson(errorResponse, ErrorResponse::class.java)
                val errorMessage = errorObject?.message ?: "System error (HTTP ${response.code()}"

                Result.Error(
                    exception = Exception("HTTP Error ${response.code()}"),
                    message = errorMessage
                )

            } else {
                Result.Error(
                    exception = Exception("Unknown Error"),
                    message = "Unexpected Server Error"
                )
            }
        } catch (e: Exception ) {
            Result.Error(
                exception = e,
                message = "Server Error"
            )
        }
    }

    /***/
    override suspend fun login(
        email: String,
        password: String,
    ): Result<Boolean> {
        return try {
            val response = authAPI.login(LoginRequest(email,password))
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.Error(e, e.message?:"Unknown Error")
        }
    }

    /***/
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            val response = authAPI.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown Error")
        }
    }

    /***/
    override suspend fun guestLogin(): Result<Boolean> {
        return try {
            val response = authAPI.loginAsGuest()
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.Error(e, e.message ?:"Unknown Error")
        }
    }

    /***/
    override suspend fun logout(): Result<Boolean> {
        return try {
            val response = authAPI.logout(
                LogoutRequest(
                    tokenManager.getRefreshToken() ?: "",
                    tokenManager.getAccessToken() ?: ""
                )
            )
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.Error(e, e.message ?:"Unknown Error")
        }
    }

    /***/
    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken()!=null
    }

    /***/
    override suspend fun getCurrentUser(): UserDTO? {
        return try {
            tokenManager.getUser()?.let { userJson ->
                gson.fromJson(userJson, UserDTO::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /***/
    override suspend fun observeLoginState(): Flow<String?> = tokenManager.observeAccessToken()

}