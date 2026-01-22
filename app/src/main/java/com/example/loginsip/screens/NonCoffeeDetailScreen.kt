package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun NonCoffeeDetailScreen(
    navController: NavHostController,
    imageRes: Int,
    drinkName: String,
    description: String,
    priceSmall: Int,
    priceMedium: Int,
    priceLarge: Int
) {

    // ---------------- STATE ----------------
    var cupSize by remember { mutableStateOf("Medium") }
    var sugarLevel by remember { mutableStateOf("Normal") }
    var quantity by remember { mutableStateOf(1) }

    // ---------------- PRICE LOGIC ----------------
    val basePrice = when (cupSize) {
        "Small" -> priceSmall
        "Medium" -> priceMedium
        "Large" -> priceLarge
        else -> priceMedium
    }

    val totalPrice = basePrice * quantity

    // ---------------- BACKGROUND ----------------
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color.White,
            Color(0xFFDDB892)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(horizontal = 16.dp)
            .padding(top = 40.dp)
    ) {

        // ---------------- IMAGE ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(Color.LightGray, RoundedCornerShape(22.dp))
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = drinkName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---------------- TITLE & DESCRIPTION ----------------
        Text(
            text = drinkName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = description,
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(12.dp))

        // ---------------- PRICE ----------------
        Text(
            text = "₱$totalPrice.00",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6F4E37)
        )

        Spacer(Modifier.height(28.dp))

        // ---------------- CUP SIZE (RADIO) ----------------
        Text(
            text = "Cup Size",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        // ---------------- SUGAR LEVEL (BUTTONS) ----------------
        Text(
            text = "Sweetness Level",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("No Sweetener", "Less", "Normal", "Extra").forEach { level ->
                Button(
                    onClick = { sugarLevel = level },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (sugarLevel == level) Color(0xFFFFC085)
                            else Color.LightGray
                    ),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = level,
                        color = if (sugarLevel == level) Color.White else Color.Black,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // ---------------- QUANTITY ----------------
        Text(
            text = "Quantity",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (quantity > 1) quantity-- },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("-")
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = quantity.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(16.dp))

            Button(
                onClick = { quantity++ },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+")
            }
        }

        Spacer(Modifier.weight(1f))

        // ---------------- BOTTOM ACTIONS ----------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Back", color = Color.Black)
            }

            Button(
                onClick = { navController.navigate("chooseOption") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC085)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Proceed", color = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}