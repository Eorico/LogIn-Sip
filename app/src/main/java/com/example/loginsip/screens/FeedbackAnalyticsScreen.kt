package com.example.loginsip.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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

data class Feedback(
    val userName: String,
    val rating: Float,
    val comment: String,
    val orderType: String
)

@Composable
fun FeedbackAnalyticsScreen(navController: NavHostController) {

    val db = FirebaseFirestore.getInstance()
    var feedbackList by remember { mutableStateOf<List<Feedback>>(emptyList()) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Fetch feedback from Firestore
    LaunchedEffect(Unit) {
        db.collection("orders")
            .whereEqualTo("appFeedbackGiven", true)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = mutableListOf<Feedback>()
                snapshot.documents.forEach { doc ->
                    val userName = doc.getString("userId") ?: "Unknown User"
                    val rating = doc.getLong("appRating")?.toFloat() ?: 0f
                    val comment = doc.getString("appComment") ?: ""
                    val orderType = doc.getString("orderType") ?: "-"
                    list.add(Feedback(userName, rating, comment, orderType))
                }
                feedbackList = list
            }
    }

    // -------------------- DRAWER --------------------
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
                    text = "Pending Orders",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("staff_dashboard") {
                            popUpTo("feedback_analytics") { inclusive = true }
                        }
                    }
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") { popUpTo(0) }
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

            Column(modifier = Modifier.padding(16.dp)) {

                // ☰ MENU BUTTON
                Text(
                    text = "☰",
                    fontSize = 28.sp,
                    modifier = Modifier.clickable {
                        scope.launch { drawerState.open() }
                    }
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Feedback Details",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6F4E37)
                )

                Spacer(Modifier.height(16.dp))

                if (feedbackList.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(feedbackList) { feedback ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = feedback.userName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "Order Type: ${feedback.orderType}",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "Rating: ${"%.1f".format(feedback.rating)} ⭐",
                                        fontSize = 16.sp,
                                        color = Color(0xFF6F4E37)
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    if (feedback.comment.isNotEmpty()) {
                                        Text(
                                            text = "Comment: ${feedback.comment}",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text("No feedback yet", fontSize = 16.sp)
                }
            }
        }
    }
}
