package com.example.loginsip.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenSourceAiService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta/") // base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: OpenSourceAi_Api = retrofit.create(OpenSourceAi_Api::class.java)
}
