package com.example.loginsip.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ChatScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var userMessage by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var isTyping by remember { mutableStateOf(false) }

    val coffeeGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color(0xFFFFF8F0),
            Color(0xFFDDB892)
        )
    )

    // Initial greeting
    LaunchedEffect(Unit) {
        try {
            val greetingsRes = ChatService.api.sendMessage(ChatRequest(""))
            delay(Random.nextLong(3000, 5000))
            messages = messages + ("ai" to greetingsRes.aiText)
        } catch (_: Exception) {
            messages = messages + ("ai" to "AI service is temporarily unavailable.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(coffeeGradient)
            .padding(16.dp)
    ) {

        // -------- TOP BAR --------
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("← Back", color = Color(0xFF6F4E37))
            }

            Text(
                text = "AI Barista",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6F4E37)
            )
        }

        Spacer(Modifier.height(12.dp))

        // -------- CHAT WINDOW --------
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            messages.forEach { (role, text) ->
                val isUser = role == "user"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (isUser) Color(0xFFFFC085) else Color(0xFFEFE6DA),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(12.dp)
                            .widthIn(max = 260.dp)
                    ) {
                        Text(
                            text = text,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            if (isTyping) {
                Text(
                    text = "AI is typing...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // -------- INPUT ROW --------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask the barista... ☕") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    if (userMessage.isBlank()) return@Button

                    val msgToSend = userMessage
                    messages = messages + ("user" to msgToSend)
                    userMessage = ""
                    isTyping = true

                    scope.launch {
                        try {
                            delay(Random.nextLong(3000, 5000))

                            val backendRes =
                                ChatService.api.sendMessage(ChatRequest(text = msgToSend))

                            if (backendRes.aiText.lowercase() == "fallback") {
                                val chimeraRes =
                                    OpenSourceAiService.api.getResponse(
                                        apiKey = "AIzaSyDdW9YbPXUPE7HhdfPgJHcqaJamOaXSmZI",
                                        OpenSourceAi_Api_Request(msgToSend)
                                    )

                                val finalAiText =
                                    chimeraRes.firstOrNull()?.generated_text
                                        ?: "AI service is temporarily unavailable."

                                messages = messages + ("ai" to finalAiText)
                            } else {
                                messages = messages + ("ai" to backendRes.aiText)

                                backendRes.recommendations?.let { aiData ->
                                    if (aiData.isNotEmpty()) {
                                        var recommendationText =
                                            "Here are some suggestions for you:\n"
                                        aiData.forEachIndexed { idx, item ->
                                            recommendationText +=
                                                "${idx + 1}. ${item.name} - ${item.reason}\n"
                                        }
                                        messages =
                                            messages + ("ai" to recommendationText)
                                    }
                                }

                                backendRes.followUps?.let {
                                    messages = messages + ("ai" to it)
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            messages =
                                messages + ("ai" to "AI service is temporarily unavailable.")
                        } finally {
                            isTyping = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6F4E37)
                )
            ) {
                Text("Send", color = Color.White)
            }
        }
    }
}