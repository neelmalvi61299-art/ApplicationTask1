package com.networkmodule.interceptors


import com.example.core_module.manager.TokenManager
import com.facebook.stetho.inspector.protocol.module.Network
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

open class AuthenticationInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // Build new request
        val builder = request.newBuilder()
        builder.header("Accept", "application/json") // if necessary, say to consume JSON
        builder.header("PLATFORM", "Android")

        val token = getAccessToken() // save token of this request for future
        setAuthHeader(builder, token) // write current token to request

        request = builder.build() // overwrite old request
        val response = chain.proceed(request) // perform request, here original request will be executed

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) { // if unauthorized
            synchronized(this) {
                // perform all 401 in sync blocks, to avoid multiply token updates
                val currentToken = getAccessToken() // get currently stored token

                if (currentToken != null && currentToken == token) { // compare current token with token that was stored before, if it was not updated - do update
                    logout()
                }
            }
        }

        return response
    }

    private fun getAccessToken(): String? {
        return tokenManager.token
    }

    private fun logout() {
        tokenManager.reset()
        // logout your user
    }

    open fun setAuthHeader(builder: Request.Builder, token: String?) {
        if (!token.isNullOrEmpty())
//            builder.header("Authorization", String.format("Authorization", token)) // Sudesh changed this for working purpose
            builder.header("Authorization", "Bearer ${token.removePrefix("Bearer ")}")
    }
}