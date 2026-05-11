package com.mycom.chan.domain.repository

import android.net.Uri
import com.mycom.chan.data.mapper.PostMapper
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Comment
import com.mycom.chan.domain.model.FeedPage
import com.mycom.chan.domain.model.Post

interface PostsRepository {
    suspend fun getPosts(postMapper: PostMapper = PostMapper()): Result<List<Post>>
    suspend fun getFeed(limit: Int = 20, after: String? = null): Result<FeedPage>
    suspend fun likePost(postId: String): Result<Unit>
    suspend fun getComments(postId: String, page: Int = 1, size: Int = 20): Result<List<Comment>>
    suspend fun addComment(postId: String, content: String): Result<Unit>
    suspend fun addReply(postId: String, commentId: String, content: String): Result<Unit>
    suspend fun createPost(caption: String,
                           tags: List<String>,
                           isPublic: Boolean,
                           uris: List<Uri> = emptyList()): Result<Post>
}