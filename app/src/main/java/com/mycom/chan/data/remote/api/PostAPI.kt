package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.response.BaseResponse
import com.mycom.chan.data.remote.dto.response.PostsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PostAPI {
    @GET("posts/feed")
    suspend fun getPosts(
        @QueryMap params: Map<String, String>
    ): Response<BaseResponse<PostsResponse>>
}