package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inandoutlaundry.data.remote.PaymentResponse
import com.example.inandoutlaundry.viewmodel.LaundryViewModel
import kotlinx.coroutines.launch

/**
 * PaymentScreen handles sandbox/demo payment processing (DemoCard, DemoEFT, Cash).
 * Developed by third-year software development student.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    orderId: String,
    laundryViewModel: LaundryViewModel,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedMethod by remember { mutableStateOf("DemoCard") }
    var isLoading by remember { mutableStateOf(false) }
    var paymentResult by remember { mutableStateOf<PaymentResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val orderIdInt = orderId.filter { it.isDigit() }.toIntOrNull() ?: 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demo Payment - Order $orderId") },
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
            Text(text = "Select Payment Method (Sandbox / Demo)", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedMethod == "DemoCard",
                    onClick = { selectedMethod = "DemoCard" },
                    label = { Text("Demo Card") }
                )
                FilterChip(
                    selected = selectedMethod == "DemoEFT",
                    onClick = { selectedMethod = "DemoEFT" },
                    label = { Text("Demo EFT") }
                )
                FilterChip(
                    selected = selectedMethod == "Cash",
                    onClick = { selectedMethod = "Cash" },
                    label = { Text("Cash") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    coroutineScope.launch {
                        val result = laundryViewModel.submitPayment(orderIdInt, selectedMethod)
                        isLoading = false
                        result.fold(
                            onSuccess = { paymentResult = it },
                            onFailure = { errorMessage = it.localizedMessage }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(Icons.Filled.Payment, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Process Demo Payment")
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            if (paymentResult != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Payment Successful!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        Text(text = "Transaction Ref: ${paymentResult!!.transactionReference}")
                        Text(text = "Method: ${paymentResult!!.paymentMethod}")
                        Text(text = "Amount: R${paymentResult!!.amount}")
                        Text(text = "Status: ${paymentResult!!.paymentStatus}")
                        Text(text = "Demo Mode: ${paymentResult!!.isDemo}")
                    }
                }
            }
        }
    }
}
