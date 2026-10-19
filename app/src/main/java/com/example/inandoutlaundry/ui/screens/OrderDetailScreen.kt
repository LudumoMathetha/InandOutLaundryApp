package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inandoutlaundry.viewmodel.LaundryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    laundryViewModel: LaundryViewModel,
    onBack: () -> Unit
) {
    val orders by laundryViewModel.orders.collectAsState()
    val order = orders.find { it.id == orderId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details - $orderId") },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (order == null) {
                Text("Order not found.")
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Service: ${order.serviceName}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Price: R${order.price}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                        Text(text = "Current Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Pickup / Scheduled Date: ${order.pickupDate}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Delivery Address: ${order.deliveryAddress}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Notes: ${order.notes}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
