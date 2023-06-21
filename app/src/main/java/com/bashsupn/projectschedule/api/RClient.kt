package com.bashsupn.projectschedule.api

import android.content.Context
import com.bashsupn.projectschedule.sharedpreferences.RequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RClient {
    private const val BASE_URL = "https://api.putraeu.my.id/api/"
    fun Create(context: Context):Api{
        val retrofit= Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(Api::class.java)
    }


    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor(context))
            .build()
    }
}