package com.example.loginsip.screens

import android.widget.Toast
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

    // ===== FEEDBACK STATES =====
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var rating by remember { mutableIntStateOf(0) }
    var feedbackText by remember { mutableStateOf("") }
    var currentOrderId by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {

        // -------- BACK --------
        Text(
            text = "Back",
            color = Color(0xFF6F4E37),
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 16.dp)
                .clickable { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.height(40.dp))

            Text(
                text = "Choose Your Option",
                fontSize = 28.sp,
                color = Color(0xFF6F4E37),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(Modifier.height(40.dp))

            // -------- DELIVERY --------
            Button(
                onClick = {
                    placeOrder(
                        navController,
                        itemName,
                        cupSize,
                        sugarLevel,
                        quantity,
                        "Delivery"
                    ) { orderId ->
                        currentOrderId = orderId
                        showFeedbackDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC085))
            ) {
                Text("Delivery", fontSize = 18.sp, color = Color.White)
            }

            Spacer(Modifier.height(24.dp))

            // -------- ON SITE --------
            Button(
                onClick = {
                    placeOrder(
                        navController,
                        itemName,
                        cupSize,
                        sugarLevel,
                        quantity,
                        "On Site"
                    ) { orderId ->
                        currentOrderId = orderId
                        showFeedbackDialog = true
                    }
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

        // ===== FEEDBACK DIALOG =====
        if (showFeedbackDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Rate App Experience") },
                text = {
                    Column {
                        Text("How was the app functionality?")

                        Spacer(Modifier.height(12.dp))

                        Row {
                            repeat(5) { index ->
                                Text(
                                    text = if (index < rating) "⭐" else "☆",
                                    fontSize = 26.sp,
                                    modifier = Modifier.clickable {
                                        rating = index + 1
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = feedbackText,
                            onValueChange = { feedbackText = it },
                            placeholder = { Text("Optional feedback") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            FirebaseFirestore.getInstance()
                                .collection("orders")
                                .document(currentOrderId)
                                .update(
                                    mapOf(
                                        "appRating" to rating,
                                        "appComment" to feedbackText, // ✅ FIXED
                                        "appFeedbackGiven" to true
                                    )
                                )

                            showFeedbackDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("Submit")
                    }
                }
            )
        }
    }
}

// ---------------- ORDER FUNCTION ----------------
private fun placeOrder(
    navController: NavHostController,
    itemName: String,
    cupSize: String,
    sugarLevel: String,
    quantity: Int,
    orderType: String,
    onOrderSuccess: (String) -> Unit
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
        "status" to "Pending",
        "appFeedbackGiven" to false
    )

    db.collection("orders")
        .add(order)
        .addOnSuccessListener { documentRef ->
            Toast.makeText(
                navController.context,
                "Order placed ($orderType)",
                Toast.LENGTH_SHORT
            ).show()

            NotificationStore.notifications.add(
                "$quantity x $itemName ordered ($orderType)"
            )

            onOrderSuccess(documentRef.id)
        }
        .addOnFailureListener {
            Toast.makeText(
                navController.context,
                "Order failed",
                Toast.LENGTH_SHORT
            ).show()
        }
}
