package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun SnacksDetailScreen(
    navController: NavHostController,
    imageRes: Int,
    snackName: String,
    description: String,
    price: Int
) {
    // ---------------- STATE ----------------
    var quantity by remember { mutableStateOf(1) }
    var extras by remember { mutableStateOf("None") }

    val totalPrice = price * quantity

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
                contentDescription = snackName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---------------- TITLE & DESCRIPTION ----------------
        Text(
            text = snackName,
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

        // ---------------- EXTRAS (BUTTONS) ----------------
        Text(
            text = "Extras",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("None", "Cheese", "Chocolate", "Caramel").forEach { option ->
                Button(
                    onClick = { extras = option },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (extras == option) Color(0xFFFFC085)
                            else Color.LightGray
                    ),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = option,
                        color = if (extras == option) Color.White else Color.Black,
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
                onClick = {
                    val encodedItemName =
                        URLEncoder.encode(snackName, StandardCharsets.UTF_8.toString())

                    navController.navigate(
                        "chooseOption/$encodedItemName/N-A/N-A/$quantity"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC085)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Proceed", color = Color.White)
            }
        }
    }
}