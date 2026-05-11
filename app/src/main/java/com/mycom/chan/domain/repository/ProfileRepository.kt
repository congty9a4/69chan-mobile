package com.mycom.chan.domain.repository

import android.net.Uri
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfileByUserId(userId: String): Result<Profile>
    suspend fun getProfileById(profileId: Int): Result<Profile>
    suspend fun profileExists(userId: String): Result<Boolean>
    suspend fun createProfile(
        userId: String,
        bio: String? = null,
        country: String? = null,
        birthday: String? = null,
        phone: String? = null,
        fullname: String? = null,
        avatarUri: Uri? = null,
        coverUri: Uri? = null
    ): Result<Profile>
    suspend fun updateProfile(
        userId: String,
        bio: String? = null,
        location: String? = null,
        birthday: String? = null,
        phone: String? = null,
        avatarUrl: String? = null,
        coverPhotoUrl: String? = null
    ): Result<Profile>
}
