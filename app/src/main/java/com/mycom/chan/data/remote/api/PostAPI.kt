package com.mycom.chan.data.remote.api

import com.mycom.chan.data.remote.dto.model.ContentDTO
import com.mycom.chan.data.remote.dto.request.CommentRequest
import com.mycom.chan.data.remote.dto.response.BaseResponse
import com.mycom.chan.data.remote.dto.response.CommentResponse
import com.mycom.chan.data.remote.dto.response.CursorPageResponse
import com.mycom.chan.data.remote.dto.response.PageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PostAPI {

    @GET("feeds/")
    suspend fun getPosts(
        @QueryMap params: Map<String, String>
    ): Response<CursorPageResponse<ContentDTO>>

    @GET("feeds/")
    suspend fun getFeeds(
        @Query("limit") limit: Int = 20,
        @Query("after") after: String? = null
    ): Response<CursorPageResponse<ContentDTO>>

    @PATCH("posts/{id}/like")
    suspend fun likePost(@Path("id") postId: String): Response<Unit>

    @GET("posts/{postId}/comments")
    suspend fun getComments(
        @Path("postId") postId: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<BaseResponse<PageResponse<CommentResponse>>>

    @POST("posts/{postId}/comments")
    suspend fun addComment(
        @Path("postId") postId: String,
        @Body request: CommentRequest
    ): Response<Unit>

    @POST("posts/{postId}/comments/{commentId}")
    suspend fun addReply(
        @Path("postId") postId: String,
        @Path("commentId") commentId: String,
        @Body request: CommentRequest
    ): Response<Unit>

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Part("post") post: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<BaseResponse<ContentDTO>>
}
