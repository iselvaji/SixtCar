package com.sixt.sixtcar.model.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.sixt.sixtcar.utils.Constants.Companion.URL_BASE
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

open class ApiClientHelper {

    companion object {

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createClient())
            .build()

        fun getInterface(): ApiService? {
            return retrofit.create(ApiService::class.java)
        }

        private fun createClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
               // .readTimeout(ServerHelper.TIME_OUT, TimeUnit.SECONDS)
                builder.addNetworkInterceptor(StethoInterceptor())
            return builder.build()
        }
    }
}