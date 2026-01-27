package com.example.loginsip.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun StaffDashboardScreen(navController: NavHostController) {

    val db = FirebaseFirestore.getInstance()
    val orders = remember { mutableStateListOf<Pair<String, Map<String, Any>>>() }

    // 🔹 Drawer state + coroutine scope (FIXES suspend error)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 🔥 REAL-TIME LISTENER (ONLY PENDING ORDERS)
    LaunchedEffect(Unit) {
        db.collection("orders")
            .whereEqualTo("status", "Pending")
            .addSnapshotListener { snapshot, _ ->
                orders.clear()
                snapshot?.documents?.forEach { doc ->
                    doc.data?.let { data ->
                        orders.add(doc.id to data)
                    }
                }
            }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .background(Color(0xFF6F4E37))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Staff Panel",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFDDB892), Color.White)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // ☰ MENU BUTTON
                Text(
                    text = "☰",
                    fontSize = 28.sp,
                    modifier = Modifier.clickable {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Pending Orders",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6F4E37)
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn {
                    items(orders) { (docId, order) ->

                        // ✅ Extract fields from order map
                        val itemName = order["itemName"]?.toString() ?: "Item"
                        val cupSize = order["cupSize"]?.toString() ?: "-"
                        val sugarLevel = order["sugarLevel"]?.toString() ?: "-"
                        val quantity = (order["quantity"] as? Long)?.toInt() ?: 1
                        val orderType = order["orderType"]?.toString() ?: "-"

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF3E0)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(Modifier.padding(14.dp)) {

                                Text(
                                    text = itemName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Text("Cup: $cupSize")
                                Text("Sugar: $sugarLevel")
                                Text("Qty: $quantity")
                                Text("Type: $orderType")

                                Spacer(Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        // 1️⃣ Update the order status in Firestore
                                        db.collection("orders")
                                            .document(docId)
                                            .update("status", "Completed")

                                        // 2️⃣ Remove the corresponding notification
                                        val notifText = "$quantity x $itemName ordered ($orderType)"
                                        NotificationStore.notifications.remove(notifText)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFC085)
                                    )
                                ) {
                                    Text("Mark as Completed", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
