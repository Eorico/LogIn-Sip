package com.example.loginsip.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.8:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ChatApi = retrofit.create(ChatApi::class.java)
}