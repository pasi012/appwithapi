package com.example.appwithapi

import com.example.appwithapi.activities.ApiService
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object ApiClient {
    private const val BASE_URL = "https://reqres.in/api/"

    val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}