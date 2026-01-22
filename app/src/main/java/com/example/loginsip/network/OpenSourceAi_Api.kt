package com.example.loginsip.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class OpenSourceAi_Api_Request(val inputs: String)
data class OpenSourceAi_Api_Response(val generated_text: String)

interface OpenSourceAi_Api {
    @POST("models/gemini-2.5-flash:generateContent")
    suspend fun getResponse(
        @Header("x-goog-api-key") apiKey: String,
        @Body request: OpenSourceAi_Api_Request
    ): List<OpenSourceAi_Api_Response>
}
