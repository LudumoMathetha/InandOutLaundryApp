package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & User Guide") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "In and Out Laundry - User Guide", style = MaterialTheme.typography.headlineSmall)
            
            Text(
                text = "Welcome to In and Out Laundry! This mobile application allows you to schedule professional laundry pickups, track order statuses in real-time, and manage your account preferences.",
                style = MaterialTheme.typography.bodyMedium
            )

            Divider()

            Text(text = "1. Registration & Login", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Register with your full name, email, phone number, and a secure password. You can also sign in instantly using Google Single Sign-On (SSO).",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(text = "2. Booking Services", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Browse through our 10+ professional laundry services ranging from standard wash & fold to dry cleaning and sneaker care. Select a service and tap 'Book Now'.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(text = "3. Tracking Orders", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "View all your active and completed orders in real-time from the dashboard or orders list. Tap any order for full detailed information.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(text = "4. Settings & Support", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Customize your preferences including dark mode, push notifications, and default address via the Settings screen.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
