package com.example.samplecomposeapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://6784c99a1ec630ca33a5a3ec.mockapi.io/"

    private val okHttpClient = OkHttpClient()

    /*
    "by lazy" is a delegated property that provides lazy initialization.
    It is a way to delay the initialization of a property until
    it is actually accessed for the first time.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}