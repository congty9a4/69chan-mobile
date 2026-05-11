package com.mycom.chan.data.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.mycom.chan.data.mapper.toProfile
import com.mycom.chan.data.remote.api.ProfileAPI
import com.mycom.chan.data.remote.dto.model.Result
import com.mycom.chan.data.remote.dto.request.ProfileCreateRequest
import com.mycom.chan.data.remote.dto.request.ProfileUpdateRequest
import com.mycom.chan.domain.model.Profile
import com.mycom.chan.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class ProfileRepositoryImple @Inject constructor(
    private val profileAPI: ProfileAPI,
    private val gson: Gson,
    @ApplicationContext private val context: Context
) : ProfileRepository, BaseRepository() {

    override suspend fun getProfileByUserId(userId: String): Result<Profile> =
        handleResponse(
            apiCall = { profileAPI.getProfileByUserId(userId) },
            transform = { it.toProfile() }
        )

    override suspend fun getProfileById(profileId: Int): Result<Profile> =
        handleResponse(
            apiCall = { profileAPI.getProfileById(profileId) },
            transform = { it.toProfile() }
        )

    override suspend fun profileExists(userId: String): Result<Boolean> =
        handleResponse(
            apiCall = { profileAPI.profileExists(userId) },
            transform = { it }
        )

    override suspend fun createProfile(
        userId: String,
        bio: String?,
        country: String?,
        birthday: String?,
        phone: String?,
        fullname: String?,
        avatarUri: Uri?,
        coverUri: Uri?
    ): Result<Profile> {
        val profileJson = gson.toJson(ProfileCreateRequest(bio, country, birthday, phone, fullname))
        val profileBody = profileJson.toRequestBody("application/json".toMediaType())
        val avatarPart = avatarUri?.toMultipartPart("avatar")
        val coverPart = coverUri?.toMultipartPart("coverImage")
        return handleResponse(
            apiCall = { profileAPI.createProfile(
                userId = userId,
                profile = profileBody,
                avatar = avatarPart,
                coverImage = coverPart
            ) },
            transform = { it.toProfile() }
        )
    }

    override suspend fun updateProfile(
        userId: String,
        bio: String?,
        location: String?,
        birthday: String?,
        phone: String?,
        avatarUrl: String?,
        coverPhotoUrl: String?
    ): Result<Profile> =
        handleResponse(
            apiCall = {
                profileAPI.updateProfile(
                    userId,
                    ProfileUpdateRequest(
                        bio = bio,
                        location = location,
                        birthday = birthday,
                        phone = phone,
                        avatarUrl = avatarUrl,
                        coverPhotoUrl = coverPhotoUrl
                    )
                )
            },
            transform = { it.toProfile() }
        )

    private fun Uri.toMultipartPart(name: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(this) ?: "image/*"
        val bytes = contentResolver.openInputStream(this)!!.use { it.readBytes() }
        return MultipartBody.Part.createFormData(
            name, "upload_${System.currentTimeMillis()}", bytes.toRequestBody(mimeType.toMediaType())
        )
    }
}

