package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inandoutlaundry.viewmodel.AuthViewModel
import com.example.inandoutlaundry.viewmodel.LaundryViewModel

/**
 * DashboardScreen displays active orders, quick actions, and navigation access.
 * Developed by third-year software development student.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    laundryViewModel: LaundryViewModel,
    onNavigateToServices: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToContact: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val orders by laundryViewModel.orders.collectAsState()

    val activeOrdersCount = orders.count { it.status != "Completed" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("In and Out Laundry - Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = {
                    IconButton(onClick = onNavigateToContact) {
                        Icon(Icons.Filled.SupportAgent, contentDescription = "Contact Support")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = onNavigateToHelp) {
                        Icon(Icons.Filled.Help, contentDescription = "Help")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.LocalLaundryService, contentDescription = "Services") },
                    label = { Text("Services") },
                    selected = false,
                    onClick = onNavigateToServices
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Orders") },
                    label = { Text("Orders") },
                    selected = false,
                    onClick = onNavigateToOrders
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = onNavigateToProfile
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Hello, ${currentUser?.fullName ?: "Valued Customer"}!",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Email: ${currentUser?.email ?: ""}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You have $activeOrdersCount active laundry order(s) in progress.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onNavigateToServices,
                        modifier = Modifier.weight(1.0f)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Book Service")
                    }
                    OutlinedButton(
                        onClick = onNavigateToOrders,
                        modifier = Modifier.weight(1.0f)
                    ) {
                        Icon(Icons.Filled.ListAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View Orders")
                    }
                }
            }

            item {
                Text(
                    text = "Recent Orders",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(orders.take(3)) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = order.serviceName, style = MaterialTheme.typography.titleSmall)
                            Text(text = "R${order.price}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Delivery: ${order.deliveryAddress}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out")
                }
            }
        }
    }
}
