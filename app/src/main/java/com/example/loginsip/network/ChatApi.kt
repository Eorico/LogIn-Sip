package com.example.loginsip.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("api/recommended")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}