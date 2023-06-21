package com.bashsupn.projectschedule.sharedpreferences

import android.content.Context
import okhttp3.Interceptor

class RequestInterceptor (context: Context) : Interceptor {
    private val prefManager = PrefManager(context)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()

        // Jika token ada di session manager, token sisipkan di request header
        prefManager.fetchAccessToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}