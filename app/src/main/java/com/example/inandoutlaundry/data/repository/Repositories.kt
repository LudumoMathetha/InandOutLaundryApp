package com.example.inandoutlaundry.data.repository

import com.example.inandoutlaundry.data.model.LaundryOrder
import com.example.inandoutlaundry.data.model.LaundryService
import com.example.inandoutlaundry.data.model.User
import com.example.inandoutlaundry.data.model.UserPreference
import com.example.inandoutlaundry.utils.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * AuthRepository manages user authentication state, registration, login hashing, and Google SSO.
 * Adheres to the Repository architectural pattern separating data sources from ViewModels.
 */
class AuthRepository {
    // In-memory data store seeded with a default test account
    private val users = mutableMapOf<String, User>(
        "user@inandout.com" to User(
            id = "user_1",
            email = "user@inandout.com",
            passwordHash = SecurityUtils.hashPassword("Password123"),
            fullName = "John Doe",
            phoneNumber = "0821234567"
        )
    )

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun register(email: String, password: String, fullName: String, phoneNumber: String): Result<User> {
        if (users.containsKey(email)) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val newUser = User(
            id = "user_" + UUID.randomUUID().toString().take(8),
            email = email,
            passwordHash = SecurityUtils.hashPassword(password), // Secure hashing
            fullName = fullName,
            phoneNumber = phoneNumber
        )
        users[email] = newUser
        _currentUser.value = newUser
        return Result.success(newUser)
    }

    fun login(email: String, password: String): Result<User> {
        val user = users[email] ?: return Result.failure(Exception("Invalid email or password."))
        val hashed = SecurityUtils.hashPassword(password)
        if (user.passwordHash != hashed) {
            return Result.failure(Exception("Invalid email or password."))
        }
        _currentUser.value = user
        return Result.success(user)
    }

    fun loginWithGoogle(email: String, fullName: String): User {
        val existing = users[email]
        if (existing != null) {
            _currentUser.value = existing
            return existing
        }
        val newUser = User(
            id = "google_" + UUID.randomUUID().toString().take(8),
            email = email,
            passwordHash = SecurityUtils.hashPassword("GoogleSSO123!"),
            fullName = fullName,
            phoneNumber = "0800000000",
            ssoProvider = "Google"
        )
        users[email] = newUser
        _currentUser.value = newUser
        return newUser
    }

    fun logout() {
        _currentUser.value = null
    }
}

/**
 * LaundryRepository manages laundry services and orders.
 * Requirement: Seeded with at least 10 records per required data table (11 services & 10 orders).
 */
class LaundryRepository {
    // Exactly 11 professional services seeded (meets requirement of >= 10 records)
    private val _services = listOf(
        LaundryService(1, "Standard Wash & Fold", "Everyday machine wash, tumble dry, and neat folding.", 120.0, 24, "Wash"),
        LaundryService(2, "Dry Cleaning - Suit", "Professional solvent dry cleaning and steam pressing for suits.", 180.0, 48, "Dry Clean"),
        LaundryService(3, "Express Ironing", "Hand ironed garments returned on hangers within 6 hours.", 90.0, 6, "Ironing"),
        LaundryService(4, "Delicate Silk & Wool", "Gentle hand wash with eco-friendly organic detergents.", 150.0, 36, "Special"),
        LaundryService(5, "Comforter & Blanket", "Deep steam sanitization and heavy-duty drying.", 220.0, 48, "Household"),
        LaundryService(6, "Sneaker Cleaning", "Deep cleaning, deodorizing, and restoration for footwear.", 130.0, 72, "Footwear"),
        LaundryService(7, "Leather & Suede Care", "Specialized conditioning and stain removal.", 300.0, 96, "Special"),
        LaundryService(8, "Curtain & Drapes Deep Clean", "Thorough steam cleaning and dust mite removal.", 250.0, 72, "Household"),
        LaundryService(9, "Wedding Dress Preservation", "Meticulous hand cleaning and acid-free boxing.", 600.0, 168, "Special"),
        LaundryService(10, "Eco-Friendly Wash", "Hypoallergenic, fragrance-free washing with zero plastic packaging.", 140.0, 24, "Wash"),
        LaundryService(11, "Industrial Workwear", "Heavy-duty stain removal and oil treatment.", 160.0, 48, "Commercial")
    )

    // Exactly 10 initial orders seeded (meets requirement of >= 10 records)
    private val _orders = MutableStateFlow<List<LaundryOrder>>(
        listOf(
            LaundryOrder("ORD-1001", "user_1", 1, "Standard Wash & Fold", 120.0, "Completed", "2025-05-01", "124 Main Road, Cape Town", "No fabric softener"),
            LaundryOrder("ORD-1002", "user_1", 2, "Dry Cleaning - Suit", 180.0, "Out for Delivery", "2025-05-03", "124 Main Road, Cape Town", "Handle with care"),
            LaundryOrder("ORD-1003", "user_1", 3, "Express Ironing", 90.0, "Ready", "2025-05-04", "124 Main Road, Cape Town", "Extra starch"),
            LaundryOrder("ORD-1004", "user_1", 5, "Comforter & Blanket", 220.0, "Processing", "2025-05-05", "124 Main Road, Cape Town", "Stain on corner"),
            LaundryOrder("ORD-1005", "user_1", 6, "Sneaker Cleaning", 130.0, "Received", "2025-05-06", "124 Main Road, Cape Town", "White leather sneakers"),
            LaundryOrder("ORD-1006", "user_1", 1, "Standard Wash & Fold", 120.0, "Completed", "2025-04-20", "124 Main Road, Cape Town", "Standard"),
            LaundryOrder("ORD-1007", "user_1", 4, "Delicate Silk & Wool", 150.0, "Completed", "2025-04-22", "124 Main Road, Cape Town", "Hand wash only"),
            LaundryOrder("ORD-1008", "user_1", 8, "Curtain & Drapes Deep Clean", 250.0, "Completed", "2025-04-25", "124 Main Road, Cape Town", "Living room curtains"),
            LaundryOrder("ORD-1009", "user_1", 10, "Eco-Friendly Wash", 140.0, "Processing", "2025-05-06", "124 Main Road, Cape Town", "Eco detergents"),
            LaundryOrder("ORD-1010", "user_1", 2, "Dry Cleaning - Suit", 180.0, "Received", "2025-05-07", "124 Main Road, Cape Town", "Winter coat")
        )
    )
    val orders: StateFlow<List<LaundryOrder>> = _orders.asStateFlow()

    fun getServices(): List<LaundryService> = _services

    fun createOrder(userId: String, serviceId: Int, address: String, notes: String): LaundryOrder {
        val service = _services.find { it.id == serviceId } ?: _services.first()
        val newOrder = LaundryOrder(
            id = "ORD-" + (1000 + _orders.value.size + 1),
            userId = userId,
            serviceId = service.id,
            serviceName = service.name,
            price = service.price,
            status = "Received",
            pickupDate = "Today",
            deliveryAddress = address,
            notes = notes
        )
        _orders.value = listOf(newOrder) + _orders.value
        return newOrder
    }
}

/**
 * SettingsRepository manages user preferences persistence.
 */
class SettingsRepository {
    private val _preference = MutableStateFlow(
        UserPreference(
            userId = "user_1",
            darkMode = false,
            notificationsEnabled = true,
            defaultAddress = "124 Main Road, Cape Town"
        )
    )
    val preference: StateFlow<UserPreference> = _preference.asStateFlow()

    fun updatePreferences(darkMode: Boolean, notificationsEnabled: Boolean, address: String) {
        _preference.value = UserPreference(
            userId = _preference.value.userId,
            darkMode = darkMode,
            notificationsEnabled = notificationsEnabled,
            defaultAddress = address
        )
    }
}
