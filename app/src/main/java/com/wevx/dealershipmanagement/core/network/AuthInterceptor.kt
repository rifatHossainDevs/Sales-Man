
package com.wevx.dealershipmanagement.core.network

import com.wevx.dealershipmanagement.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenManager.getAccessToken()

        val requestBuilder = originalRequest.newBuilder()

        if (!accessToken.isNullOrBlank()) {
            val formattedToken = if (accessToken.startsWith("Bearer ")) {
                accessToken
            } else {
                "Bearer $accessToken"
            }

            requestBuilder.header("Authorization", formattedToken)
        }

        return chain.proceed(requestBuilder.build())
    }
}
