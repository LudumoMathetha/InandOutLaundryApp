package com.example.inandoutlaundry.ui.navigation

/**
 * Defines navigation route constants for Jetpack Navigation Compose.
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Services : Screen("services")
    object Orders : Screen("orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Help : Screen("help")
}
