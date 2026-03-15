package com.mycom.chan.data.local

import com.mycom.chan.domain.repository.TokenManager
import com.mycom.chan.domain.repository.TokenAuthenticator
import com.mycom.chan.domain.repository.TokenRefresher
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class ImpleTokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefresher: TokenRefresher
) : TokenAuthenticator
{
    /**
     * The TokenAuthenticator is the solution to handle failed request, this function will be called when meet 401.
     * authenticate() will first check if there had been more than 3 retries. Avoid sending too many request
     * if not, we get the old token from the failed request and then "refresh" the tokens
     * if refresh successfully, we use the new token to resend the failed request
     * if not, simply log out
     * **/
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >3 ) return null //no more than 3 retries

        val failedToken = response.request.header("Authorization")?.removePrefix("Bearer ")
        val isRefreshSuccessful = runBlocking {
            tokenRefresher.refreshToken(failedToken)
        }

        if (isRefreshSuccessful) {
            val newToken = runBlocking { tokenManager.getAccessToken() }
            if (newToken != null) {
                return response
                    .request.newBuilder()
                    .header("Authorization", "Bearer ${newToken}")
                    .build()

            }
            else {
                runBlocking { tokenRefresher.handleRefreshFailure() }
            }
        }
        return null
    }

    /**
     * if request failed, okhttp will retry. if still failed, that failed request will be stored in priorResponse
     * if there are more than 3 priorResponse, we will stop the authenticator**/
    private fun responseCount(response: Response):Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }

}