package com.example.loginsip.network

data class Recommendation(
    val name: String,
    val reason: String
)


data class ChatResponse (
    val emotion: String,
    val message: String,
    val recommendations: List<Recommendation>? = null,
    val followUps: String?,
    val aiText: String = "",
)