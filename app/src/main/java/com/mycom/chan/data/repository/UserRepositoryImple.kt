package com.mycom.chan.data.repository

import com.mycom.chan.data.mapper.toSearchResult
import com.mycom.chan.data.remote.api.UserAPI
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.SearchResult
import com.mycom.chan.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImple @Inject constructor(
    private val userAPI: UserAPI
) : UserRepository, BaseRepository() {

    override suspend fun followUser(userId: String): Result<Unit> =
        handleResponse(apiCall = { userAPI.followUser(userId) }, transform = {})

    override suspend fun unfollowUser(userId: String): Result<Unit> =
        handleResponse(apiCall = { userAPI.unfollowUser(userId) }, transform = {})

    override suspend fun search(query: String, filter: String?): Result<SearchResult> =
        handleResponse(
            apiCall = { userAPI.search(query, filter) },
            transform = { it.toSearchResult() }
        )
}