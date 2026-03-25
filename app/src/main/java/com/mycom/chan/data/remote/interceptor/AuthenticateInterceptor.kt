package com.mycom.chan.data.remote.interceptor

import com.mycom.chan.domain.repository.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticateInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    /**
     * Interceptor will automatically add headers for every request by:
        - Create a duplicate of the original request
        - Add required headers, token,...
        - Send it to the server
     * We need to exclude those endpoints that do not need access token.
     **/
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/json")

        val noAuthHeader = request.header("No-Auth")

        if (noAuthHeader == "true") {
            requestBuilder.removeHeader("No-Auth")
        } else {
            val token = runBlocking { tokenManager.getAccessToken() }
            if (!token.isNullOrBlank()) {
                requestBuilder.header("Authorization","Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

}