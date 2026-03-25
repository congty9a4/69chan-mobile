package com.mycom.chan.di

import com.mycom.chan.core.network.auth.TokenAuthenticatorImple
import com.mycom.chan.data.local.auth.TokenManagerImple
import com.mycom.chan.core.network.auth.TokenRefresherImple
import com.mycom.chan.data.repository.ImpleAuthRepository
import com.mycom.chan.data.repository.ImplePostRepository
import com.mycom.chan.domain.repository.AuthRepository
import com.mycom.chan.domain.repository.PostsRepository
import com.mycom.chan.domain.repository.TokenAuthenticator
import com.mycom.chan.domain.repository.TokenManager
import com.mycom.chan.domain.repository.TokenRefresher
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
        tokenManagerImple: TokenManagerImple
    ): TokenManager

    @Binds
    @Singleton
    abstract fun bindTokenAuthenticator(
        tokenAuthenticatorImple: TokenAuthenticatorImple
    ): TokenAuthenticator

    @Binds
    @Singleton
    abstract fun bindTokenRefresher(
        tokenRefresherImple: TokenRefresherImple
    ): TokenRefresher

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impleAuthRepository: ImpleAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        implePostRepository: ImplePostRepository
    ): PostsRepository
}
