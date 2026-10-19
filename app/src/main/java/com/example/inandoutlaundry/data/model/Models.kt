package com.example.inandoutlaundry.data.model

/**
 * Data models representing the core entities of the In and Out Laundry system.
 * Designed for relational consistency and Kotlin data class immutability.
 */

/**
 * Represents an authenticated or registered user in the system.
 * Stores securely hashed passwords rather than plaintext.
 */
data class User(
    val id: String,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val phoneNumber: String,
    val ssoProvider: String? = null // Tracks if registered via Google SSO
)

/**
 * Represents a professional laundry or dry-cleaning service offering.
 * Seeded with 10+ distinct services as per academic requirements.
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
 * Tracks order status (Received, Processing, Ready, Out for Delivery, Completed).
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
 * Represents user-specific application preferences (Dark mode, notifications, default address).
 */
data class UserPreference(
    val userId: String,
    val darkMode: Boolean,
    val notificationsEnabled: Boolean,
    val defaultAddress: String
)
