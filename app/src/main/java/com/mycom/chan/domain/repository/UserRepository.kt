package com.mycom.chan.domain.repository

import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.SearchResult

interface UserRepository {
    suspend fun followUser(userId: String): Result<Unit>
    suspend fun unfollowUser(userId: String): Result<Unit>
    suspend fun search(query: String, filter: String? = null): Result<SearchResult>
}