package com.mycom.chan.data.local

import com.mycom.chan.data.remote.api.AuthAPI
import com.mycom.chan.domain.repository.TokenManager
import com.mycom.chan.data.remote.dto.request.RefreshRequest
import com.mycom.chan.domain.repository.TokenRefresher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ImpleTokenRefresher @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApi: AuthAPI
) : TokenRefresher {
    private val refreshMutex = Mutex() //making sure only one fresher at a time
    /**
     * failedToken is required to make sure: IF there were 2 request to refresh, only one will be sent
     * making sure refresh token and access token are not null nor blank
     * after all of that, we send the request, receive new tokens and save it
     * if failed with code 401 and 403 (unauthorized), we will call handler, which will log the user out and required a new login attempt
     * if failed because server error, we still counted as failed but won't require user to log in, they just have to retry
     **/
    override suspend fun refreshToken(failedToken :String?): Boolean = refreshMutex.withLock {
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null && accessToken != failedToken) {
            return@withLock true
        }
        val refreshToken = tokenManager.getRefreshToken()
        if (accessToken == null || refreshToken.isNullOrBlank()) {
            return@withLock false
        }
        try {
            val request = RefreshRequest (
                refreshToken = refreshToken
            )
            val response = authApi.refreshToken(request)
            if (response.isSuccessful && response.body()?.success == true) {
                val newToken = response.body()?.body
                if (newToken!=null) {
                    tokenManager.saveAccessToken(newToken.token)
                    if(newToken.refreshToken.isNotBlank()) {
                        tokenManager.saveRefreshToken(newToken.refreshToken)
                    }
                    return@withLock true
                }
            }
            else if (response.code() == 401 || response.code() == 403) {
                handleRefreshFailure()

            }
            return@withLock false
        } catch (e: Exception) {
            e.printStackTrace()
            return@withLock false
        }

    }

    /**
     * decision making**/
    override suspend fun shouldRefreshToken(): Boolean {
        return !tokenManager.getRefreshToken().isNullOrBlank()
    }

    /**
     * Clear account when refreshing failed, which will log the user out and require the user to log back in to continue **/
    override suspend fun handleRefreshFailure() {
        tokenManager.clearToken()
    }
}