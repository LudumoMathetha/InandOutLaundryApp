package com.example.inandoutlaundry.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inandoutlaundry.ui.screens.*
import com.example.inandoutlaundry.viewmodel.AuthViewModel
import com.example.inandoutlaundry.viewmodel.LaundryViewModel
import com.example.inandoutlaundry.viewmodel.SettingsViewModel

/**
 * NavGraph configures all screen destinations and navigation arguments for Jetpack Compose Navigation.
 */
@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    laundryViewModel: LaundryViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Login Screen Route
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Registration Screen Route
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Dashboard Screen Route
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                authViewModel = authViewModel,
                laundryViewModel = laundryViewModel,
                onNavigateToServices = {
                    navController.navigate(Screen.Services.route)
                },
                onNavigateToOrders = {
                    navController.navigate(Screen.Orders.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToHelp = {
                    navController.navigate(Screen.Help.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // Services Catalog Route
        composable(Screen.Services.route) {
            ServiceCatalogScreen(
                authViewModel = authViewModel,
                laundryViewModel = laundryViewModel,
                onBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.Orders.route)
                }
            )
        }

        // Orders List Route
        composable(Screen.Orders.route) {
            OrderTrackingScreen(
                laundryViewModel = laundryViewModel,
                onBack = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                }
            )
        }

        // Order Detail Route with Argument
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(
                orderId = orderId,
                laundryViewModel = laundryViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // User Profile Route
        composable(Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // Settings Route
        composable(Screen.Settings.route) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Help & Documentation Route
        composable(Screen.Help.route) {
            HelpScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
