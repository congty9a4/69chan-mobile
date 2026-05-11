package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.request.LoginRequest
import com.mycom.chan.data.remote.dto.request.LogoutRequest
import com.mycom.chan.data.remote.dto.request.RefreshRequest
import com.mycom.chan.data.remote.dto.request.RegisterRequest
import com.mycom.chan.data.remote.dto.response.AuthResponse
import com.mycom.chan.data.remote.dto.response.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPI {
    @Headers("No-Auth: true")
    @GET("auth/guest")
    suspend fun loginAsGuest() : Response<BaseResponse<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest) : Response<BaseResponse<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest) : Response<BaseResponse<Unit>>

    @Headers("No-Auth: true")
    @POST("auth/verify-email")
    suspend fun verifyEmail(
        @Query("email") email: String,
        @Query("otp") otp: String
    ): Response<BaseResponse<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("auth/resend-otp")
    suspend fun resendOtp(@Query("email") email: String): Response<BaseResponse<Unit>>

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshRequest) : Response<BaseResponse<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("auth/refresh-token")
    fun refreshTokenSync(@Body request: RefreshRequest): Call<BaseResponse<AuthResponse>>

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<BaseResponse<AuthResponse>>
}
