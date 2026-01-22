package com.example.loginsip.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun DashboardScreen(navController: NavHostController, userName: String = "User") {
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Gradient background
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFDDB892),
            Color.White,
            Color(0xFFDDB892)
        )
    )

    // -------------------- CATEGORY DATA --------------------
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
        R.drawable.coffee1, R.drawable.snacks1, R.drawable.noncoffee1
    )

    var selectedCategory by remember { mutableStateOf("All Menu") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {

        // -------------------- TOP BAR --------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            TopBarWithDropdown(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                modifier = Modifier.align(Alignment.TopStart)
            )

            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.notification_icon),
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        // -------------------- MAIN CONTENT --------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp) // leave space for top icons
        ) {

            // --------- FIXED TOP: Greeting + Search Bar + Categories ---------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    "Log-In: Sip & Connect",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onBackground.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append("Good Day, ")
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF6F4E37),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(userName)
                        }
                        append("!")
                    },
                    fontSize = 20.sp,
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.height(16.dp))

                // Row with Search Bar + Floating Chatbot Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Search Bar
                    Box(
                        modifier = Modifier
                            .weight(1f) // take most of the space
                            .height(52.dp)
                            .background(colors.surface, RoundedCornerShape(16.dp))
                    )

                    // Floating Chatbot Icon
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color(0xFFFFC085), shape = RoundedCornerShape(26.dp))
                            .clickable { navController.navigate("chat") }, // clickable ends here
                        contentAlignment = Alignment.Center // <-- must be here, not in modifier chain
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.chatbot_icon),
                            contentDescription = "AI Chatbot",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Categories (fixed, not scrollable)
                val categories = listOf("All Menu", "Coffee", "Non-Coffee", "Snacks", "Light Bites")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories) { category ->
                        val isSelected = category == selectedCategory
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(0xFFFFC085) else colors.surface,
                                    RoundedCornerShape(24.dp)
                                )
                                .clickable {
                                    selectedCategory = category
                                    scope.launch {
                                        listState.scrollToItem(0)
                                    }
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

                Spacer(Modifier.height(8.dp))
            }

            // --------- SCROLLABLE GRID CONTENT ---------
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {

                // ---------- CATEGORY GRID ----------
                item {
                    Text(selectedCategory, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(16.dp))

                    val gridImages =
                        if (selectedCategory == "All Menu") categoryImages.values.flatten()
                        else categoryImages[selectedCategory] ?: emptyList()

                    val gridHeight = ((gridImages.size + 1) / 2 * 210).dp

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.height(gridHeight)
                    ) {
                        items(gridImages.size) { index ->
                            val imageRes = gridImages[index]

                            ImageCard(
                                imageRes = imageRes,
                                onClick = {
                                    when (imageRes) {
                                        R.drawable.coffee1 -> navController.navigate("coffee1")
                                        R.drawable.coffee2 -> navController.navigate("coffee2")
                                        R.drawable.coffee3 -> navController.navigate("coffee3")
                                        R.drawable.coffee4 -> navController.navigate("coffee4")

                                        R.drawable.noncoffee1 -> navController.navigate("noncoffee1")
                                        R.drawable.noncoffee2 -> navController.navigate("noncoffee2")
                                        R.drawable.noncoffee3 -> navController.navigate("noncoffee3")
                                        R.drawable.noncoffee4 -> navController.navigate("noncoffee4")

                                        R.drawable.snacks1 -> navController.navigate("snacks1")
                                        R.drawable.snacks2 -> navController.navigate("snacks2")
                                        R.drawable.snacks3 -> navController.navigate("snacks3")
                                        R.drawable.snacks4 -> navController.navigate("snacks4")

                                        R.drawable.lightbites1 -> navController.navigate("lightbites1")
                                        R.drawable.lightbites2 -> navController.navigate("lightbites2")
                                        R.drawable.lightbites3 -> navController.navigate("lightbites3")
                                        R.drawable.lightbites4 -> navController.navigate("lightbites4")
                                    }
                                }
                            )
                        }
                    }
                }

                // ---------- BEST SELLERS ----------
                item {
                    Spacer(Modifier.height(16.dp))
                    Text("Best Sellers!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(16.dp))

                    val bestHeight = ((bestSellersImages.size + 1) / 2 * 210).dp

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.height(bestHeight)
                    ) {
                        items(bestSellersImages.size) { index ->
                            val imageRes = bestSellersImages[index]

                            ImageCard(
                                imageRes = imageRes,
                                onClick = {
                                    when (imageRes) {
                                        R.drawable.coffee1 -> navController.navigate("coffee1")
                                        R.drawable.noncoffee1 -> navController.navigate("noncoffee1")
                                        R.drawable.snacks1 -> navController.navigate("snacks1")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

                // -------------------- IMAGE CARD --------------------
                @Composable
                fun ImageCard(imageRes: Int, onClick: () -> Unit = {}) {
                    // Use a Box to handle clickable + shape
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f) // make it square (1:1 ratio)
                            .clickable { onClick() }
                            .clip(RoundedCornerShape(20.dp)) // clip the image to rounded corners
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop, // fills the whole box
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

// -------------------- TOP BAR WITH DROPDOWN --------------------
@Composable
fun TopBarWithDropdown(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.background(Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dropdownbutton),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(150.dp)
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
}