package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inandoutlaundry.viewmodel.LaundryViewModel

/**
 * OrderTrackingScreen lists customer orders with status badges and payment actions.
 * Developed by third-year software development student.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    laundryViewModel: LaundryViewModel,
    onBack: () -> Unit,
    onOrderClick: (String) -> Unit,
    onPayClick: (String) -> Unit
) {
    val orders by laundryViewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Laundry Orders (${orders.size})") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOrderClick(order.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Order #${order.id}", style = MaterialTheme.typography.titleSmall)
                            Text(text = "R${order.price}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Service: ${order.serviceName}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                        Text(text = "Address: ${order.deliveryAddress}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onPayClick(order.id) },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Filled.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Pay Now")
                            }
                        }
                    }
                }
            }
        }
    }
}
