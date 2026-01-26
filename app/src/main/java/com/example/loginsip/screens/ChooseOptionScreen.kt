package com.example.loginsip.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ChooseOptionScreen(
    navController: NavHostController,
    itemName: String,
    cupSize: String,
    sugarLevel: String,
    quantity: Int
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color.White,
            Color(0xFFDDB892)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {

        // -------- BACK TEXT (UNCHANGED) --------
        Text(
            text = "Back",
            color = Color(0xFF6F4E37),
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // -------- TITLE --------
            Text(
                text = "Choose Your Option",
                fontSize = 28.sp,
                color = Color(0xFF6F4E37),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // -------- DELIVERY BUTTON --------
            Button(
                onClick = {
                    placeOrder(
                        navController,
                        itemName,
                        cupSize,
                        sugarLevel,
                        quantity,
                        "Delivery"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC085))
            ) {
                Text("Delivery", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -------- ONSITE BUTTON --------
            Button(
                onClick = {
                    placeOrder(
                        navController,
                        itemName,
                        cupSize,
                        sugarLevel,
                        quantity,
                        "On Site"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC085))
            ) {
                Text("On Site", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}

// ---------------- GLOBAL NOTIFICATIONS ----------------
val orderNotifications = mutableStateListOf<String>()

// ---------------- ORDER FUNCTION ----------------
private fun placeOrder(
    navController: NavHostController,
    itemName: String,
    cupSize: String,
    sugarLevel: String,
    quantity: Int,
    orderType: String
) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown_user"

    val order = hashMapOf(
        "userId" to userId,
        "itemName" to itemName,
        "cupSize" to cupSize,
        "sugarLevel" to sugarLevel,
        "quantity" to quantity,
        "orderType" to orderType,
        "status" to "Pending"
    )

    db.collection("orders")
        .add(order)
        .addOnSuccessListener {
            Toast.makeText(
                navController.context,
                "Order placed ($orderType)",
                Toast.LENGTH_SHORT
            ).show()

            // ===== ADD NOTIFICATION =====
            orderNotifications.add("$quantity x $itemName ordered ($orderType)")

            navController.navigate("dashboard/$userId") {
                popUpTo("dashboard/$userId") { inclusive = true }
            }
        }
        .addOnFailureListener {
            Toast.makeText(
                navController.context,
                "Order failed",
                Toast.LENGTH_SHORT
            ).show()
        }
}