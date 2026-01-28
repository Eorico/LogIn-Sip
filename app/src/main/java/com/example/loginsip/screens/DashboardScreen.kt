package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loginsip.R
import kotlinx.coroutines.launch

// ---------------- SEARCH MATCH HELPER ----------------
private fun imageMatchesSearch(imageRes: Int, query: String): Boolean {
    if (query.isBlank()) return true

    val name = when (imageRes) {
        R.drawable.coffee1 -> "Dark Mocha Latte"
        R.drawable.coffee2 -> "Caramel Macchiato"
        R.drawable.coffee3 -> "Spanish Latte"
        R.drawable.coffee4 -> "Coffee Jelly"

        R.drawable.noncoffee1 -> "Chocolate"
        R.drawable.noncoffee2 -> "Matcha Latte"
        R.drawable.noncoffee3 -> "Cookies and Cream"
        R.drawable.noncoffee4 -> "Strawberry"

        R.drawable.snacks1 -> "Fries"
        R.drawable.snacks2 -> "Cheesy Samyang"
        R.drawable.snacks3 -> "Ham & Pepperoni Panini"
        R.drawable.snacks4 -> "Nutella Croffle"

        R.drawable.lightbites1 -> "Choco Java Chip (coffee-based)"
        R.drawable.lightbites2 -> "Biscoff Dream (non-coffee)"
        R.drawable.lightbites3 -> "Belgian Chocolate (non-coffee)"
        R.drawable.lightbites4 -> "Strawberry (non-coffee)"

        else -> ""
    }

    return name.contains(query.lowercase())
}

// ---------------- DASHBOARD ----------------
@Composable
fun DashboardScreen(
    navController: NavHostController,
    userName: String = "User"
) {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Menu") }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color.White,
            Color(0xFFDDB892)
        )
    )

    val categoryImages = mapOf(
        "Coffee" to listOf(
            R.drawable.coffee1,
            R.drawable.coffee2,
            R.drawable.coffee3,
            R.drawable.coffee4
        ),
        "Non-Coffee" to listOf(
            R.drawable.noncoffee1,
            R.drawable.noncoffee2,
            R.drawable.noncoffee3,
            R.drawable.noncoffee4
        ),
        "Snacks" to listOf(
            R.drawable.snacks1,
            R.drawable.snacks2,
            R.drawable.snacks3,
            R.drawable.snacks4
        ),
        "Light Bites" to listOf(
            R.drawable.lightbites1,
            R.drawable.lightbites2,
            R.drawable.lightbites3,
            R.drawable.lightbites4
        )
    )

    val bestSellersImages = listOf(
        R.drawable.coffee1,
        R.drawable.noncoffee1,
        R.drawable.snacks1
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {

        // ---------------- TOP BAR ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopBarWithDropdown(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )

            // -------- NOTIFICATION ICON (SMALLER) --------
            var showNotifications by remember { mutableStateOf(false) }
            var hasUnread by remember { mutableStateOf(NotificationStore.notifications.isNotEmpty()) }

            Box {
                IconButton(
                    onClick = {
                        showNotifications = !showNotifications
                        if (showNotifications) hasUnread = false
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.notification_icon),
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    if (hasUnread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, RoundedCornerShape(50))
                                .align(Alignment.TopEnd)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showNotifications,
                    onDismissRequest = { showNotifications = false }
                ) {
                    if (NotificationStore.notifications.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No notifications yet") },
                            onClick = { showNotifications = false }
                        )
                    } else {
                        NotificationStore.notifications.asReversed().forEach { notif ->
                            DropdownMenuItem(
                                text = { Text(notif, fontSize = 14.sp) },
                                onClick = { showNotifications = false }
                            )
                        }
                    }
                }
            }
        }

        // ---------------- CONTENT ----------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                Text(
                    "Log-In: Sip & Connect",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF3E2723)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    buildAnnotatedString {
                        append("Good Day, ")
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF6F4E37),
                                fontWeight = FontWeight.Bold
                            )
                        ) { append(userName) }
                        append("!")
                    },
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(16.dp))

                // SEARCH + CHAT
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search drinks & snacks") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color(0xFFFFC085), RoundedCornerShape(26.dp))
                            .clickable { navController.navigate("chat") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chatbot_icon),
                            contentDescription = "Chatbot",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // CATEGORIES
                val categories = listOf("All Menu", "Coffee", "Non-Coffee", "Snacks", "Frappe")

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val isSelected = category == selectedCategory

                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(0xFFFFC085) else colors.surface,
                                    RoundedCornerShape(24.dp)
                                )
                                .clickable {
                                    selectedCategory = category
                                    scope.launch { listState.scrollToItem(0) }
                                }
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Text(
                                category,
                                color = if (isSelected) Color.White else colors.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }

            // ---------------- SCROLLABLE ----------------
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // ✅ CATEGORY TITLE
                item {
                    Text(
                        selectedCategory,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    val actualCategory =
                        if (selectedCategory == "Frappe") "Light Bites" else selectedCategory

                    val gridImages =
                        if (selectedCategory == "All Menu") {
                            categoryImages.values.flatten()
                        } else {
                            categoryImages[actualCategory] ?: emptyList()
                        }.filter { imageMatchesSearch(it, searchQuery) }

                    val gridHeight = ((gridImages.size + 1) / 2 * 210).dp

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .height(gridHeight)
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(gridImages.size) { index ->
                            ImageCard(
                                imageRes = gridImages[index],
                                onClick = {
                                    navController.navigate(
                                        when (gridImages[index]) {
                                            R.drawable.coffee1 -> "coffee1"
                                            R.drawable.coffee2 -> "coffee2"
                                            R.drawable.coffee3 -> "coffee3"
                                            R.drawable.coffee4 -> "coffee4"
                                            R.drawable.noncoffee1 -> "noncoffee1"
                                            R.drawable.noncoffee2 -> "noncoffee2"
                                            R.drawable.noncoffee3 -> "noncoffee3"
                                            R.drawable.noncoffee4 -> "noncoffee4"
                                            R.drawable.snacks1 -> "snacks1"
                                            R.drawable.snacks2 -> "snacks2"
                                            R.drawable.snacks3 -> "snacks3"
                                            R.drawable.snacks4 -> "snacks4"
                                            R.drawable.lightbites1 -> "lightbites1"
                                            R.drawable.lightbites2 -> "lightbites2"
                                            R.drawable.lightbites3 -> "lightbites3"
                                            else -> "lightbites4"
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

                item {
                    Text(
                        "Best Sellers!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .height(210.dp)
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(bestSellersImages.size) { index ->
                            val image = bestSellersImages[index]
                            ImageCard(
                                imageRes = image,
                                onClick = {
                                    navController.navigate(
                                        when (image) {
                                            R.drawable.coffee1 -> "coffee1"
                                            R.drawable.noncoffee1 -> "noncoffee1"
                                            else -> "snacks1"
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- IMAGE CARD ----------------
@Composable
fun ImageCard(imageRes: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// ---------------- TOP BAR ----------------
@Composable
fun TopBarWithDropdown(
    onLogout: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.dropdownbutton),
                contentDescription = "Menu",
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Log Out", color = Color.Red) },
                onClick = {
                    expanded = false
                    onLogout()
                }
            )
        }
    }
}
