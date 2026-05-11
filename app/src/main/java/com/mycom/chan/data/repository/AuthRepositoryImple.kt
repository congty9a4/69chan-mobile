package com.mycom.chan.data.repository

import com.google.gson.Gson
import com.mycom.chan.data.mapper.toAuthResult
import com.mycom.chan.data.remote.api.AuthAPI
import com.mycom.chan.data.mapper.toUser
import com.mycom.chan.data.remote.dto.response.AuthResponse
import com.mycom.chan.data.remote.dto.request.LoginRequest
import com.mycom.chan.domain.repository.AuthRepository
import com.mycom.chan.domain.repository.TokenManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.request.LogoutRequest
import kotlin.jvm.java
import com.mycom.chan.data.remote.dto.request.RegisterRequest
import com.mycom.chan.di.MainClient
import com.mycom.chan.domain.model.Authentication
import com.mycom.chan.domain.model.User

class AuthRepositoryImple @Inject constructor(
    private val tokenManager: TokenManager,
    @MainClient val authAPI: AuthAPI,
    private val gson: Gson,
) : BaseRepository(), AuthRepository {

    private suspend fun saveAuthResult(authResponseDTO: AuthResponse): Authentication {
        val authResult = authResponseDTO.toAuthResult()
        tokenManager.saveRefreshToken(authResult.refreshToken)
        tokenManager.saveUser(gson.toJson(authResponseDTO.user.toUser()))
        tokenManager.saveAccessToken(authResult.accessToken)
        return authResult
    }

    /***/
    override suspend fun login(
        email: String,
        password: String,
    ): Result<Authentication> {
        return try {
            handleResponse(
                apiCall = { authAPI.login(LoginRequest(email, password)) },
                transform = { saveAuthResult(it) }
            )
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown Error")
        }
    }

    /***/
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Result<Unit> = handleUnitResponse(
        apiCall = { authAPI.register(RegisterRequest(username, email, password)) }
    )

    /***/
    override suspend fun guestLogin(): Result<Authentication> {
        return try {
            handleResponse(
                apiCall = { authAPI.loginAsGuest() },
                transform = { saveAuthResult(it) }
            )
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown Error")
        }
    }

    /***/
    override suspend fun verifyEmail(email: String, otp: String): Result<Authentication> {
        return try {
            handleResponse(
                apiCall = { authAPI.verifyEmail(email, otp) },
                transform = { saveAuthResult(it) }
            )
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Unknown Error")
        }
    }

    /***/
    override suspend fun resendOtp(email: String): Result<Unit> = handleUnitResponse(
        apiCall = { authAPI.resendOtp(email) }
    )

    /***/
    override suspend fun logout(): Result<Boolean> {
        val refreshToken = tokenManager.getRefreshToken() ?: ""
        tokenManager.clearToken()
        try { authAPI.logout(LogoutRequest(refreshToken)) } catch (_: Exception) { }
        return Result.Success(true)
    }

    /***/
    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken()!=null
    }

    /***/
    override suspend fun getCurrentUser(): User? {
        return try {
            tokenManager.getUser()?.let { userJson ->
                gson.fromJson(userJson, User::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /***/
    override suspend fun observeLoginState(): Flow<String?> = tokenManager.observeAccessToken()

}