package com.blub.sofi.utils

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authorisedRequest = chain.request().newBuilder()
            .addHeader("Authorization", getAccessToken()).build();
        return chain.proceed(authorisedRequest)
    }

    private fun getAccessToken(): String {
        return "Client-ID 126701cd8332f32"
    }
}
