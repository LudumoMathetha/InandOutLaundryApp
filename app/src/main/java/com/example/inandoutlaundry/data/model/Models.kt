package com.example.inandoutlaundry.data.model

/**
 * Data models representing the core entities of the In and Out Laundry system.
 * Designed for relational consistency and Kotlin data class immutability.
 * Developed by third-year software development student.
 */

/**
 * Represents an authenticated or registered user in the system.
 */
data class User(
    val id: String,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val phoneNumber: String,
    val ssoProvider: String? = null
)

/**
 * Represents a customer profile matching CustomersController in ASP.NET Core Web API.
 */
data class Customer(
    val customerId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val dateRegistered: String
)

/**
 * Represents a professional laundry or dry-cleaning service offering.
 */
data class LaundryService(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val estimatedHours: Int,
    val category: String,
    val iconName: String = "local_laundry_service"
)

/**
 * Represents a customer laundry order placed within the application.
 */
data class LaundryOrder(
    val id: String,
    val userId: String,
    val serviceId: Int,
    val serviceName: String,
    val price: Double,
    val status: String,
    val pickupDate: String,
    val deliveryAddress: String,
    val notes: String
)

/**
 * Represents user-specific application preferences.
 */
data class UserPreference(
    val userId: String,
    val darkMode: Boolean,
    val notificationsEnabled: Boolean,
    val defaultAddress: String
)
