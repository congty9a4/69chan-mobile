package com.mycom.chan.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mycom.chan.data.remote.api.AuthAPI
import com.mycom.chan.data.remote.api.ChatAPI
import com.mycom.chan.data.remote.api.NotificationAPI
import com.mycom.chan.data.remote.api.PostAPI
import com.mycom.chan.data.remote.api.ProfileAPI
import com.mycom.chan.data.remote.api.UserAPI
import com.mycom.chan.data.remote.interceptor.AuthenticateInterceptor
import com.mycom.chan.domain.repository.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private val BASE_URL get() = com.mycom.chan.BuildConfig.BASE_URL

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    // Main OkHttpClient — has auth interceptor + token authenticator
    @Provides @Singleton @MainClient
    fun provideMainOkHttpClient(
        authenticateInterceptor: AuthenticateInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authenticateInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    // Auth OkHttpClient — plain, no interceptor (used for token refresh to avoid circular dep)
    @Provides @Singleton @AuthClient
    fun provideAuthOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides @Singleton
    fun provideRetrofit(gson: Gson, @MainClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides @Singleton @AuthClient
    fun provideAuthRetrofit(gson: Gson, @AuthClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides @Singleton @MainClient
    fun provideMainAuthAPI(retrofit: Retrofit): AuthAPI = retrofit.create(AuthAPI::class.java)

    @Provides @Singleton @AuthClient
    fun provideAuthClientAuthAPI(@AuthClient retrofit: Retrofit): AuthAPI =
        retrofit.create(AuthAPI::class.java)

    @Provides @Singleton
    fun providePostAPI(retrofit: Retrofit): PostAPI = retrofit.create(PostAPI::class.java)

    @Provides @Singleton
    fun provideProfileAPI(retrofit: Retrofit): ProfileAPI = retrofit.create(ProfileAPI::class.java)

    @Provides @Singleton
    fun provideChatAPI(retrofit: Retrofit): ChatAPI = retrofit.create(ChatAPI::class.java)

    @Provides @Singleton
    fun provideNotificationAPI(retrofit: Retrofit): NotificationAPI =
        retrofit.create(NotificationAPI::class.java)

    @Provides @Singleton
    fun provideUserAPI(retrofit: Retrofit): UserAPI = retrofit.create(UserAPI::class.java)
}
