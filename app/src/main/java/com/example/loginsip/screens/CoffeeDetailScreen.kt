package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CoffeeDetailScreen(
    navController: NavHostController,
    imageRes: Int,
    coffeeName: String,
    description: String,
    priceSmall: Int,
    priceMedium: Int,
    priceLarge: Int
) {
    var cupSize by remember { mutableStateOf("Medium") }
    var sugarLevel by remember { mutableStateOf("Normal") }
    var quantity by remember { mutableStateOf(1) }

    val basePrice = when (cupSize) {
        "Small" -> priceSmall
        "Medium" -> priceMedium
        "Large" -> priceLarge
        else -> priceMedium
    }
    val totalPrice = basePrice * quantity

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color(0xFFFFF8F0),
            Color(0xFFDDB892)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {

        // ---------------- HERO IMAGE ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = coffeeName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.25f)
                            )
                        )
                    )
            )

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
            ) {
                Text("← Back", color = Color(0xFF6F4E37))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Text(
                coffeeName,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF3E2723)
            )

            Spacer(Modifier.height(6.dp))

            Text(description, fontSize = 15.sp, color = Color(0xFF5D4037))

            Spacer(Modifier.height(14.dp))

            Text(
                "₱$totalPrice.00",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6F4E37)
            )

            Spacer(Modifier.height(28.dp))

            // ---------------- CUP SIZE ----------------
            SectionTitle("Cup Size")

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                listOf("Small", "Medium", "Large").forEach { size ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { cupSize = size }
                    ) {
                        RadioButton(
                            selected = cupSize == size,
                            onClick = { cupSize = size },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFFC085)
                            )
                        )
                        Text(size)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ---------------- SUGAR LEVEL (FIXED) ----------------
            SectionTitle("Sugar Level")

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SugarButton("No Sugar", sugarLevel) { sugarLevel = "No Sugar" }
                    SugarButton("Less", sugarLevel) { sugarLevel = "Less" }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SugarButton("Normal", sugarLevel) { sugarLevel = "Normal" }
                    SugarButton("Extra", sugarLevel) { sugarLevel = "Extra" }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ---------------- QUANTITY ----------------
            SectionTitle("Quantity")

            Row(verticalAlignment = Alignment.CenterVertically) {
                QuantityButton("-", quantity > 1) { quantity-- }
                Spacer(Modifier.width(18.dp))
                Text(quantity.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(18.dp))
                QuantityButton("+") { quantity++ }
            }

            Spacer(Modifier.height(36.dp))

            Button(
                onClick = {
                    val encodedItemName =
                        URLEncoder.encode(coffeeName, StandardCharsets.UTF_8.toString())
                    val encodedCupSize =
                        URLEncoder.encode(cupSize, StandardCharsets.UTF_8.toString())
                    val encodedSugarLevel =
                        URLEncoder.encode(sugarLevel, StandardCharsets.UTF_8.toString())

                    navController.navigate(
                        "chooseOption/$encodedItemName/$encodedCupSize/$encodedSugarLevel/$quantity"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F4E37)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Proceed to Order", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

// ---------------- COMPONENTS ----------------

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun SugarButton(
    text: String,
    selected: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selected == text) Color(0xFFFFC085) else Color(0xFFE0E0E0)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text,
            color = if (selected == text) Color.White else Color.Black,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun QuantityButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC085))
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
