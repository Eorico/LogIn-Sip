package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loginsip.R

@Composable
fun OnboardingScreen(navController: NavHostController) {

    val slides = listOf(
        OnboardingSlide(
            "Welcome to Log-In: Sip & Connect",
            "Discover your perfect coffee experience",
            R.drawable.obs1
        ),
        OnboardingSlide(
            "Emotion-Based Recommendations",
            "AI chatbot suggests drinks based on your mood",
            R.drawable.obs2
        ),
        OnboardingSlide(
            "Order & Enjoy",
            "Fast, simple ordering made just for you",
            R.drawable.obs3
        )
    )

    var currentSlide by remember { mutableStateOf(0) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color(0xFFFFF3E8),
            Color(0xFFDDB892)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp)
    ) {

        val slide = slides[currentSlide]
        val playfulFont = FontFamily.Default

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(Modifier.height(12.dp))

            // -------- HERO IMAGE --------
            Image(
                painter = painterResource(id = slide.image),
                contentDescription = slide.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(28.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(32.dp))

            // -------- TEXT CONTENT --------
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = slide.title,
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = Color(0xFF6F4E37),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.8.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = slide.description,
                    fontFamily = playfulFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D4037),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(Modifier.height(32.dp))

            // -------- DOT INDICATOR --------
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                slides.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (index == currentSlide) 26.dp else 10.dp)
                            .background(
                                color =
                                    if (index == currentSlide)
                                        Color(0xFF6F4E37)
                                    else
                                        Color(0xFFBCAAA4),
                                shape = RoundedCornerShape(50)
                            )
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // -------- ACTION BUTTON --------
            Button(
                onClick = {
                    if (currentSlide < slides.size - 1) {
                        currentSlide++
                    } else {
                        navController.navigate("login") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6F4E37)
                )
            ) {
                Text(
                    text = if (currentSlide == slides.size - 1) "Get Started" else "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// -------- DATA MODEL --------
data class OnboardingSlide(
    val title: String,
    val description: String,
    val image: Int
)
