package com.mycom.chan.domain.repository

import com.mycom.chan.data.mapper.PostMapper
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Post

interface PostsRepository {
    suspend fun getPosts(postMapper: PostMapper = PostMapper()): Result<List<Post>>
}