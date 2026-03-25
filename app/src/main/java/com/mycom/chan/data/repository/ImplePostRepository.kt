package com.mycom.chan.data.repository

import com.google.gson.Gson
import com.mycom.chan.data.remote.api.PostAPI
import com.mycom.chan.data.mapper.PostMapper
import com.mycom.chan.data.mapper.toMap
import com.mycom.chan.data.mapper.toPost
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Post
import com.mycom.chan.domain.repository.PostsRepository
import javax.inject.Inject

class ImplePostRepository @Inject constructor(
    private val postAPI: PostAPI,
    private val gson: Gson
) : PostsRepository, BaseRepository() {

    /**
     * Get posts from API
     */
    override suspend fun getPosts(postMapper: PostMapper): Result<List<Post>> {
        return handleResponse(
            apiCall = {postAPI.getPosts(postMapper.toMap())},
            transform = {
                response -> response.content.map { it.toPost() }
            }
        )
    }
}