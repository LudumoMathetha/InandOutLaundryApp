package com.example.inandoutlaundry.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inandoutlaundry.viewmodel.LaundryViewModel
import kotlinx.coroutines.launch

/**
 * ContactScreen allows users to send support queries to the backend API (`POST /api/ContactMessages`).
 * Developed by third-year software development student.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    laundryViewModel: LaundryViewModel,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Support & Contact") },
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
            Text(text = "Send us a message", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it; successMessage = null },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; successMessage = null },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it; successMessage = null },
                label = { Text("Subject") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it; successMessage = null },
                label = { Text("Message") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            if (successMessage != null) {
                Text(text = successMessage!!, color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = {
                    if (fullName.isBlank() || email.isBlank() || subject.isBlank() || message.isBlank()) {
                        errorMessage = "All fields are required."
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null
                    coroutineScope.launch {
                        val result = laundryViewModel.submitContact(fullName, email, subject, message)
                        isLoading = false
                        result.fold(
                            onSuccess = { successMessage = "Message successfully submitted! We will contact you soon." },
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
                    Icon(Icons.Filled.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Query")
                }
            }
        }
    }
}
