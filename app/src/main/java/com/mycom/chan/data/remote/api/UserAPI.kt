package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.model.SearchResultDTO
import com.mycom.chan.data.remote.dto.response.BaseResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {
    @POST("users/follow/{id}")
    suspend fun followUser(
        @Path("id") userId: String
    ): Response<BaseResponse<Unit>>

    @DELETE("users/unfollow/{id}")
    suspend fun unfollowUser(
        @Path("id") userId: String
    ): Response<BaseResponse<Unit>>

    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("filter") filter: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<BaseResponse<SearchResultDTO>>
}