package com.mycom.chan.domain.repository

interface TokenRefresher {
    suspend fun refreshToken(failedToken: String?): Boolean

    suspend fun shouldRefreshToken(): Boolean

    suspend fun handleRefreshFailure()
}