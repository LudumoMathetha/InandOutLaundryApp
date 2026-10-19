package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inandoutlaundry.viewmodel.AuthViewModel
import com.example.inandoutlaundry.viewmodel.LaundryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCatalogScreen(
    authViewModel: AuthViewModel,
    laundryViewModel: LaundryViewModel,
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val services = laundryViewModel.services

    var selectedService by remember { mutableStateOf<com.example.inandoutlaundry.data.model.LaundryService?>(null) }
    var address by remember { mutableStateOf("124 Main Road, Cape Town") }
    var notes by remember { mutableStateOf("") }
    var showBookingDialog by remember { mutableStateOf(false) }
    var bookingSuccessMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Our Laundry Services (${services.size})") },
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
        ) {
            if (bookingSuccessMsg != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = bookingSuccessMsg!!, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { service ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = service.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "R${service.price}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = service.description, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Est. Turnaround: ${service.estimatedHours} hrs | Cat: ${service.category}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Button(onClick = {
                                    selectedService = service
                                    showBookingDialog = true
                                }) {
                                    Text("Book Now")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBookingDialog && selectedService != null) {
        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            title = { Text("Book: ${selectedService!!.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Price: R${selectedService!!.price}")
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Delivery / Pickup Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Special Instructions / Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val userId = currentUser?.id ?: "user_1"
                    val result = laundryViewModel.createOrder(userId, selectedService!!.id, address, notes)
                    result.fold(
                        onSuccess = {
                            bookingSuccessMsg = "Successfully booked ${selectedService!!.name}!"
                            showBookingDialog = false
                            onOrderPlaced()
                        },
                        onFailure = { /* Handle error */ }
                    )
                }) {
                    Text("Confirm Booking")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
