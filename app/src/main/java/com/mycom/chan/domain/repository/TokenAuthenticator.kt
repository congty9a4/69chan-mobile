package com.mycom.chan.domain.repository

import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

interface TokenAuthenticator {
    fun authenticate (route: Route?, response: Response): Request?

}