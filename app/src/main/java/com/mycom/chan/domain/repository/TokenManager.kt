package com.mycom.chan.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    //Save accessToken
    suspend fun saveAccessToken (accessToken: String)

    //Save refreshToken
    suspend fun saveRefreshToken (refreshToken: String)

    //Get accessToken
    suspend fun getAccessToken (): String?

    //Get refreshToken
    suspend fun getRefreshToken (): String?

    //Observe access token, this is required for UI to change when token changed
    suspend fun observeAccessToken(): Flow<String?>

    //Clear token when logged out
    suspend fun clearToken ()

    //Save user
    suspend fun saveUser(user: String)

    //Get user
    suspend fun getUser(): String?

    //Blocking APIs used in interceptor and authenticator
    fun getAccessTokenBlocking(): String?
    fun getRefreshTokenBlocking(): String?
    fun saveAccessTokenBlocking(accessToken: String)
    fun saveRefreshTokenBlocking(refreshToken: String)
    fun clearTokenBlocking()
}
