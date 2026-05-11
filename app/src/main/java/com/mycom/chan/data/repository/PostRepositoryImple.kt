package com.mycom.chan.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.mycom.chan.data.remote.api.PostAPI
import com.mycom.chan.data.mapper.PostMapper
import com.mycom.chan.data.mapper.toComment
import com.mycom.chan.data.mapper.toMap
import com.mycom.chan.data.mapper.toPost
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.request.CommentRequest
import com.mycom.chan.data.remote.dto.request.PostRequest
import com.mycom.chan.domain.model.Comment
import com.mycom.chan.domain.model.FeedPage
import com.mycom.chan.domain.model.Post
import com.mycom.chan.domain.repository.PostsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRepositoryImple @Inject constructor(
    private val postAPI: PostAPI,
    private val gson: Gson,
    @ApplicationContext private val context: Context
) : PostsRepository, BaseRepository() {

    override suspend fun getPosts(postMapper: PostMapper): Result<List<Post>> {
        return try {
            val response = postAPI.getPosts(postMapper.toMap())
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.data.map { it.toPost() })
            } else {
                Result.Error(Exception("HTTP ${response.code()}"), "HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "getPosts failed", e)
            Result.Error(e, "Connection Error")
        }
    }

    override suspend fun getFeed(limit: Int, after: String?): Result<FeedPage> {
        return try {
            val response = postAPI.getFeeds(limit, after)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.Success(
                    FeedPage(
                        posts = body.data.map { it.toPost() },
                        nextCursor = body.pageInfo.nextCursor,
                        hasNext = body.pageInfo.hasNext
                    )
                )
            } else {
                Result.Error(
                    exception = Exception("HTTP ${response.code()}"),
                    message = "Không tải được feed"
                )
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "getFeed failed", e)
            Result.Error(e, "Lỗi kết nối")
        }
    }

    override suspend fun likePost(postId: String): Result<Unit> {
        return try {
            val response = postAPI.likePost(postId)
            if (response.isSuccessful) Result.Success(Unit)
            else Result.Error(Exception("HTTP ${response.code()}"), "Like failed")
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Like failed")
        }
    }

    override suspend fun getComments(postId: String, page: Int, size: Int): Result<List<Comment>> {
        return handleResponse(
            apiCall = { postAPI.getComments(postId, page, size) },
            transform = { response -> response.content.map { it.toComment() } }
        )
    }

    override suspend fun addComment(postId: String, content: String): Result<Unit> {
        return try {
            val response = postAPI.addComment(postId, CommentRequest(content))
            if (response.isSuccessful) Result.Success(Unit)
            else Result.Error(Exception("HTTP ${response.code()}"), "Comment failed")
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Comment failed")
        }
    }

    override suspend fun addReply(postId: String, commentId: String, content: String): Result<Unit> {
        return try {
            val response = postAPI.addReply(postId, commentId, CommentRequest(content))
            if (response.isSuccessful) Result.Success(Unit)
            else Result.Error(Exception("HTTP ${response.code()}"), "Reply failed")
        } catch (e: Exception) {
            Result.Error(e, e.message ?: "Reply failed")
        }
    }

    override suspend fun createPost(caption: String, tags: List<String>, isPublic: Boolean, uris: List<Uri>): Result<Post> {
        val postJson = gson.toJson(PostRequest(caption, tags, isPublic))
        val postBody = postJson.toRequestBody("application/json".toMediaType())
        val fileParts = uris.map { it.toMultipartPart() }
        return handleResponse(
            apiCall = { postAPI.createPost(postBody, fileParts) },
            transform = { it.toPost() }
        )
    }

    private fun Uri.toMultipartPart(): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(this) ?: "image/*"
        val bytes = contentResolver.openInputStream(this)!!.use { it.readBytes() }
        val requestBody = bytes.toRequestBody(mimeType.toMediaType())
        return MultipartBody.Part.createFormData("files", "upload_${System.currentTimeMillis()}", requestBody)
    }
}
