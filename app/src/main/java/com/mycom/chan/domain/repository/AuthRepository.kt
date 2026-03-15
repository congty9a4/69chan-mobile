package com.mycom.chan.domain.repository

import com.mycom.chan.data.remote.dto.model.UserDTO
import kotlinx.coroutines.flow.Flow
import com.mycom.chan.data.remote.dto.model.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Boolean>

    suspend fun register(username: String, email: String, password: String): Result<Boolean>

    suspend fun guestLogin(): Result<Boolean>

    suspend fun logout(): Result<Boolean> {return Result.Success(true)}

    suspend fun isLoggedIn(): Boolean

    suspend fun getCurrentUser(): UserDTO?

    suspend fun observeLoginState(): Flow<String?>
}