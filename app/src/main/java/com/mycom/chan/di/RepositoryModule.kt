package com.mycom.chan.di

import com.mycom.chan.data.local.ImpleTokenManager
import com.mycom.chan.data.repository.ImpleAuthRepository
import com.mycom.chan.domain.repository.AuthRepository
import com.mycom.chan.domain.repository.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(
        impleTokenManager: ImpleTokenManager
    ): TokenManager

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impleAuthRepository: ImpleAuthRepository
    ): AuthRepository
}
