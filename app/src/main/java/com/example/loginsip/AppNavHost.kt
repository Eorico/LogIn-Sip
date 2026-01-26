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
                coffeeName = "Dark Mocha Latte",
                description = "A sophisticated blend of premium dark chocolate sauce and bold espresso, topped with velvety steamed milk. The perfect balance of bittersweet and creamy.",
                priceSmall = 129,
                priceMedium = 149,
                priceLarge = 169
            )
        }
        composable("coffee2") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee2,
                coffeeName = "Caramel Macchiato",
                description = "A timeless favorite featuring creamy steamed milk and vanilla syrup, 'marked' with a rich espresso shot poured over the top. Finished with our signature buttery caramel drizzle.",
                priceSmall = 139,
                priceMedium = 159,
                priceLarge = 179
            )
        }
        composable("coffee3") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee3,
                coffeeName = "Spanish Latte",
                description = "A unique twist on the traditional latte. We blend robust espresso with steamed milk and sweetened condensed milk to create a drink that is creamy, rich, and perfectly sweet.",
                priceSmall = 149,
                priceMedium = 169,
                priceLarge = 189
            )
        }
        composable("coffee4") {
            CoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.coffee4,
                coffeeName = "Coffee Jelly",
                description = "Hand-cut cubes of espresso gelatin served in a glass of chilled, sweetened cream. A refreshing balance of bitter coffee and smooth dairy.",
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
                drinkName = "Chocolate",
                description = "A comforting classic featuring smooth chocolate sauce blended perfectly with velvety steamed milk. Finished with a light dusting of cocoa powder on top.",
                priceSmall = 139,
                priceMedium = 159,
                priceLarge = 179
            )
        }

        composable("noncoffee2") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee2,
                drinkName = "Matcha Latte",
                description = "Premium Japanese green tea powder whisked to perfection and blended with steamed milk. An earthy, slightly sweet classic with a smooth, velvety finish",
                priceSmall = 129,
                priceMedium = 149,
                priceLarge = 169
            )
        }

        composable("noncoffee3") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee3,
                drinkName = "Cookies and Cream",
                description = "A timeless blend of creamy vanilla and crunchy chocolate sandwich cookies. Topped with a swirl of whipped cream and extra cookie crumbles for the perfect finish.",
                priceSmall = 149,
                priceMedium = 169,
                priceLarge = 189
            )
        }

        composable("noncoffee4") {
            NonCoffeeDetailScreen(
                navController = navController,
                imageRes = R.drawable.noncoffee4,
                drinkName = "Strawberry Milk",
                description = "A refreshing blend of sweet strawberry puree and cold, fresh milk. A simple, fruity classic that is perfectly pink and smooth.",
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
                snackName = "Fries",
                description = "Golden, crispy shoestring potatoes tossed lightly in sea salt. Served hot and fresh for the perfect savory snack.",
                price = 89
            )
        }
        composable("snacks2") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks2,
                snackName = "Cheesy Samyang",
                description = "A popular Korean favorite. Springy ramen noodles tossed in the signature spicy Buldak sauce, topped with a generous layer of melted cheese to balance the heat.",
                price = 99
            )
        }
        composable("snacks3") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks3,
                snackName = "Ham & Pepperoni Panini",
                description = "A hearty toasted sandwich featuring slices of savory ham and spicy pepperoni, layered with melted cheese and pressed to golden perfection between two slices of artisan bread.",
                price = 109
            )
        }
        composable("snacks4") {
            SnacksDetailScreen(
                navController = navController,
                imageRes = R.drawable.snacks4,
                snackName = "Nutella Croffle",
                description = "Our signature buttery croffle—pressed until golden and crisp—generously topped with a smooth Nutella drizzle. A perfect blend of flaky pastry and rich hazelnut chocolate.",
                price = 79
            )
        }
// ---------- LIGHT BITES ROUTES ----------
        composable("lightbites1") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites1,
                biteName = "Choco Java Chip (coffee-based)",
                description = "A rich, chocolate-based blended treat packed with premium dark chocolate chips and a hint of coffee. Topped with whipped cream and a mocha drizzle for the ultimate chocolate experience.",
                price = 99
            )
        }
        composable("lightbites2") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites2,
                biteName = "Biscoff Dream (non-coffee)",
                description = "A creamy, spiced delight featuring the iconic flavor of Lotus Biscoff. Blended with real cookie butter and topped with crunchy biscuit crumbles and a caramel swirl.",
                price = 89
            )
        }
        composable("lightbites3") {
            LightBitesDetailScreen(
                navController = navController,
                imageRes = R.drawable.lightbites3,
                biteName = "Belgian Chocolate (non-coffee)",
                description = "A luxurious treat made with authentic Belgian cocoa for a deeper, more sophisticated chocolate flavor. Perfectly steamed with silky milk for a smooth and refined finish.",
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
        // ---------------- CHOOSE OPTION ----------------
        composable(
            route = "chooseOption/{itemName}/{cupSize}/{sugarLevel}/{quantity}",
            arguments = listOf(
                navArgument("itemName") { type = NavType.StringType },
                navArgument("cupSize") { type = NavType.StringType },
                navArgument("sugarLevel") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
            val cupSize = backStackEntry.arguments?.getString("cupSize") ?: ""
            val sugarLevel = backStackEntry.arguments?.getString("sugarLevel") ?: ""
            val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1

            ChooseOptionScreen(
                navController = navController,
                itemName = itemName,
                cupSize = cupSize,
                sugarLevel = sugarLevel,
                quantity = quantity
            )
        }
    }
}