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
        val builder = request.newBuilder()
            .addHeader("Accept", "application/json")

        var sentToken = false
        if (request.header("No-Auth") == "true") {
            builder.removeHeader("No-Auth")
        } else {
            tokenManager.getAccessTokenBlocking()
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    builder.header("Authorization", "Bearer $it")
                    sentToken = true
                }
        }

        val response = chain.proceed(builder.build())

        // Backend returns 403 (not 401) when JWT is invalid/expired.
        // OkHttp Authenticator only fires on 401, so 403 never triggers refresh.
        // Clear the local token here so the app can redirect back to Login.
        if (response.code == 403 && sentToken) {
            tokenManager.clearTokenBlocking()
        }

        return response
    }

}