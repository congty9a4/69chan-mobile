package com.mycom.chan.di

import com.mycom.chan.core.network.auth.TokenAuthenticatorImple
import com.mycom.chan.data.local.auth.TokenManagerImple
import com.mycom.chan.core.network.auth.TokenRefresherImple
import com.mycom.chan.data.repository.AuthRepositoryImple
import com.mycom.chan.data.repository.ChatRepositoryImple
import com.mycom.chan.data.repository.NotificationRepositoryImple
import com.mycom.chan.data.repository.PostRepositoryImple
import com.mycom.chan.data.repository.ProfileRepositoryImple
import com.mycom.chan.data.repository.UserRepositoryImple
import com.mycom.chan.domain.repository.AuthRepository
import com.mycom.chan.domain.repository.ChatRepository
import com.mycom.chan.domain.repository.NotificationRepository
import com.mycom.chan.domain.repository.PostsRepository
import com.mycom.chan.domain.repository.ProfileRepository
import com.mycom.chan.domain.repository.TokenAuthenticator
import com.mycom.chan.domain.repository.TokenManager
import com.mycom.chan.domain.repository.TokenRefresher
import com.mycom.chan.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindTokenManager(impl: TokenManagerImple): TokenManager

    @Binds @Singleton
    abstract fun bindTokenAuthenticator(impl: TokenAuthenticatorImple): TokenAuthenticator

    @Binds @Singleton
    abstract fun bindTokenRefresher(impl: TokenRefresherImple): TokenRefresher

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImple): AuthRepository

    @Binds @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImple): PostsRepository

    @Binds @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImple): ProfileRepository

    @Binds @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImple): ChatRepository

    @Binds @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImple): NotificationRepository

    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImple): UserRepository
}
