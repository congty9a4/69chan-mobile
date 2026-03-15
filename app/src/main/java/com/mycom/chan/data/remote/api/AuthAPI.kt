package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.request.LoginRequest
import com.mycom.chan.data.remote.dto.request.LogoutRequest
import com.mycom.chan.data.remote.dto.request.RefreshRequest
import com.mycom.chan.data.remote.dto.request.RegisterRequest
import com.mycom.chan.data.remote.dto.response.AuthResponse
import com.mycom.chan.data.remote.dto.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("auth/guest")
    suspend fun loginAsGuest() : Response<BaseResponse<AuthResponse>>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest) : Response<BaseResponse<AuthResponse>>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest) : Response<BaseResponse<AuthResponse>>
    
    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshRequest) : Response<BaseResponse<AuthResponse>>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<BaseResponse<AuthResponse>>
}