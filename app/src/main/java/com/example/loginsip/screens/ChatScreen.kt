package com.example.loginsip.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loginsip.network.ChatRequest
import com.example.loginsip.network.ChatService
import com.example.loginsip.network.OpenSourceAiService
import com.example.loginsip.network.OpenSourceAi_Api_Request
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var userMessage by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Pair<String, String>>()) } // Pair<role, text>

    LaunchedEffect(Unit) {
        try {
            val greetingsRes = ChatService.api.sendMessage(ChatRequest(""))
            messages = messages + ("ai" to greetingsRes.aiText)
        } catch (e: Exception) {
            messages = messages + ("ai" to "AI service is temporarily unavailable. ${e.message}")
            print(messages)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Back")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "AI Chatbot",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // Chat Window
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFECECEC), RoundedCornerShape(16.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            messages.forEach { (role, text) ->
                Text(
                    text = if (role == "user") "You: $text" else "AI: $text",
                    fontWeight = if (role == "user") FontWeight.Medium else FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Input Row
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your message...") },
                singleLine = true
            )

            Spacer(Modifier.width(8.dp))

            Button(onClick = {
                if (userMessage.isBlank()) return@Button

                val msgToSend = userMessage
                messages = messages + ("user" to msgToSend)
                userMessage = ""

                scope.launch {
                    try {
                        val backendRes = ChatService.api.sendMessage(ChatRequest(text = msgToSend))

                         if (backendRes.aiText.lowercase() === "fallback") {
                             val chimeraRes = OpenSourceAiService.api.getResponse(
                                 apiKey = "AIzaSyDdW9YbPXUPE7HhdfPgJHcqaJamOaXSmZI",
                                 OpenSourceAi_Api_Request(msgToSend)
                             )

                             val finalAiText = chimeraRes.firstOrNull()?.generated_text
                                 ?:"AI service is temporarily unavailable."
                             messages = messages + ("ai" to finalAiText)

                         } else {
                             messages = messages + ("ai" to backendRes.aiText)

                             // Recommendations (if any)
                             backendRes.recommendations?.let { aiData ->
                                 if (aiData.isNotEmpty()) {
                                     var recommendationText = "Here are some suggestions for you:\n"
                                     aiData.forEachIndexed { idx, item ->
                                         recommendationText += "${idx + 1}. ${item.name} - ${item.reason}\n"
                                     }
                                     messages = messages + ("ai" to recommendationText)
                                 }
                             }

                             // Always add follow-ups if they exist, regardless of recommendations
                             backendRes.followUps?.let { followUp ->
                                 messages = messages + ("ai" to followUp)
                             }
                         }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        messages = messages + ("ai" to "AI service is temporarily unavailable.")
                    }
                }

            }) {
                Text("Send")
            }
        }
    }
}
