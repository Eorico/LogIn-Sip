package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun LightBitesDetailScreen(
    navController: NavHostController,
    imageRes: Int,
    biteName: String,
    description: String,
    price: Int
) {

    var quantity by remember { mutableStateOf(1) }
    val totalPrice = price * quantity

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
        // -------- IMAGE --------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(Color.LightGray, RoundedCornerShape(22.dp))
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = biteName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(20.dp))

        // -------- TITLE & DESCRIPTION --------
        Text(
            text = biteName,
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

        // -------- PRICE --------
        Text(
            text = "₱$totalPrice.00",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6F4E37)
        )

        Spacer(Modifier.height(28.dp))

        // -------- QUANTITY --------
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
            ) { Text("-") }

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
            ) { Text("+") }
        }

        Spacer(Modifier.weight(1f))

        // -------- BOTTOM ACTIONS --------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Back", color = Color.Black) }

            Button(
                onClick = { navController.navigate("chooseOption") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC085)),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Proceed", color = Color.White) }
        }

        Spacer(Modifier.height(16.dp))
    }
}