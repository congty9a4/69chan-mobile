package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.request.ProfileUpdateRequest
import com.mycom.chan.data.remote.dto.response.BaseResponse
import com.mycom.chan.data.remote.dto.response.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileAPI {

    @GET("users/{userId}/profile")
    suspend fun getProfileByUserId(
        @Path("userId") userId: String
    ): Response<BaseResponse<ProfileResponse>>

    @GET("profiles/{profileId}")
    suspend fun getProfileById(
        @Path("profileId") profileId: Int
    ): Response<BaseResponse<ProfileResponse>>

    @GET("users/{userId}/profile/exists")
    suspend fun profileExists(
        @Path("userId") userId: String
    ): Response<BaseResponse<Boolean>>

    @Multipart
    @POST("users/{userId}/profile")
    suspend fun createProfile(
        @Path("userId") userId: String,
        @Part avatar: MultipartBody.Part?,
        @Part coverImage: MultipartBody.Part?,
        @Part("profile") profile: RequestBody
    ): Response<BaseResponse<ProfileResponse>>

    @PUT("users/{userId}/profile")
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @Body request: ProfileUpdateRequest
    ): Response<BaseResponse<ProfileResponse>>
}
