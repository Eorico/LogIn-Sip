package com.example.loginsip

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loginsip.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        // ---------------- ONBOARDING & AUTH ----------------
        composable("onboarding") { OnboardingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // ---------------- CHAT ----------------
        composable("chat") { ChatScreen(navController) }

        // ---------------- DASHBOARD ----------------
        composable(
            route = "dashboard/{userName}",
            arguments = listOf(navArgument("userName") {
                type = NavType.StringType
                defaultValue = "User"
            })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "User"
            DashboardScreen(navController, userName)
        }

        // ---------------- STAFF DASHBOARD ----------------
        composable("staff_dashboard") {
            StaffDashboardScreen(navController)
        }

        // ---------------- COFFEE DETAILS ----------------
        composable("coffee1") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee1,
                coffeeName = "Coffee 1",
                description = "Rich espresso with a hint of caramel",
                priceSmall = 129,
                priceMedium = 149,
                priceLarge = 169
            )
        }
        composable("coffee2") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee2,
                coffeeName = "Coffee 2",
                description = "Smooth latte with creamy milk",
                priceSmall = 139,
                priceMedium = 159,
                priceLarge = 179
            )
        }
        composable("coffee3") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee3,
                coffeeName = "Coffee 3",
                description = "Bold dark roast for strong lovers",
                priceSmall = 149,
                priceMedium = 169,
                priceLarge = 189
            )
        }
        composable("coffee4") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee4,
                coffeeName = "Coffee 4",
                description = "Caramel macchiato with frothy milk",
                priceSmall = 159,
                priceMedium = 179,
                priceLarge = 199
            )
        }

        // -------------------- NON-COFFEE ROUTES --------------------
        composable("noncoffee1") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee1,
                drinkName = "Non Coffee 1",
                description = "Rich chocolate blended with ice and milk",
                priceSmall = 139,
                priceMedium = 159,
                priceLarge = 179
            )
        }

        composable("noncoffee2") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee2,
                drinkName = "Non Coffee 2",
                description = "Smooth matcha with warm milk",
                priceSmall = 129,
                priceMedium = 149,
                priceLarge = 169
            )
        }

        composable("noncoffee3") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee3,
                drinkName = "Non Coffee 3",
                description = "Fresh strawberry blended with yogurt",
                priceSmall = 149,
                priceMedium = 169,
                priceLarge = 189
            )
        }

        composable("noncoffee4") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee4,
                drinkName = "Non Coffee 4",
                description = "Creamy vanilla milkshake topped with whipped cream",
                priceSmall = 139,
                priceMedium = 159,
                priceLarge = 179
            )
        }
        // ------------------ SNACKS ------------------
        composable("snacks1") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks1,
                snackName = "Snack 1",
                description = "Soft and rich chocolate muffin",
                price = 89
            )
        }
        composable("snacks2") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks2,
                snackName = "Snack 2",
                description = "Flaky croissant with melted cheese",
                price = 99
            )
        }
        composable("snacks3") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks3,
                snackName = "Snack 3",
                description = "Fresh blueberry tart with crispy crust",
                price = 109
            )
        }
        composable("snacks4") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks4,
                snackName = "Snack 4",
                description = "Classic cookie loaded with chocolate chips",
                price = 79
            )
        }
// ---------- LIGHT BITES ROUTES ----------
        composable("lightbites1") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites1,
                biteName = "Light Bites 1",
                description = "Fresh sandwich with ham, cheese, and veggies",
                price = 99
            )
        }
        composable("lightbites2") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites2,
                biteName = "Light Bites 2",
                description = "Flaky croissant stuffed with creamy cheese",
                price = 89
            )
        }
        composable("lightbites3") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites3,
                biteName = "Light Bites 3",
                description = "Grilled chicken with fresh veggies wrapped",
                price = 109
            )
        }
        composable("lightbites4") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites4,
                biteName = "Light Bites 4",
                description = "Fresh seasonal fruits in a bowl",
                price = 79
            )
        }
        composable("chooseOption") { ChooseOptionScreen(navController) }
    }
}