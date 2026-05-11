package com.mycom.chan.domain.repository

import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Authentication
import com.mycom.chan.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Authentication>
    suspend fun register(username: String, email: String, password: String): Result<Unit>
    suspend fun verifyEmail(email: String, otp: String): Result<Authentication>
    suspend fun resendOtp(email: String): Result<Unit>
    suspend fun guestLogin(): Result<Authentication>
    suspend fun logout(): Result<Boolean>
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun observeLoginState(): Flow<String?>
}
