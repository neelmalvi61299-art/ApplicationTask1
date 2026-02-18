package com.networkmodule.interceptors


import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UrlValidationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()
        return if (requestUrl.isEmpty() || !(requestUrl[4].toString() == ":" || requestUrl[5].toString() == ":")) {
            throw IOException("Tamasha Error - Request URL is either empty or is having scheme different frm http or https")
        } else {
            chain.proceed(request)
        }
    }
}