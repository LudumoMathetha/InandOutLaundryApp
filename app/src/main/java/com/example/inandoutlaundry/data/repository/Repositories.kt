package com.example.inandoutlaundry.data.repository

import com.example.inandoutlaundry.data.model.LaundryOrder
import com.example.inandoutlaundry.data.model.LaundryService
import com.example.inandoutlaundry.data.model.User
import com.example.inandoutlaundry.data.model.UserPreference
import com.example.inandoutlaundry.data.remote.*
import com.example.inandoutlaundry.utils.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * AuthRepository manages authentication, JWT token storage, API calls, and local mock fallback.
 * Developed by third-year software development student.
 */
class AuthRepository {
    private val api = RetrofitClient.apiService
    private var jwtToken: String? = null

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

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(email, SecurityUtils.hashPassword(password)))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                jwtToken = body.token
                _currentUser.value = body.user
                Result.success(body.user)
            } else {
                // Fallback to local mock if API offline or test account
                localLogin(email, password)
            }
        } catch (e: Exception) {
            // Graceful offline fallback
            localLogin(email, password)
        }
    }

    private fun localLogin(email: String, password: String): Result<User> {
        val user = users[email] ?: return Result.failure(Exception("Invalid email or password."))
        val hashed = SecurityUtils.hashPassword(password)
        if (user.passwordHash != hashed) {
            return Result.failure(Exception("Invalid email or password."))
        }
        _currentUser.value = user
        return Result.success(user)
    }

    fun register(email: String, password: String, fullName: String, phoneNumber: String): Result<User> {
        if (users.containsKey(email)) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val newUser = User(
            id = "user_" + UUID.randomUUID().toString().take(8),
            email = email,
            passwordHash = SecurityUtils.hashPassword(password),
            fullName = fullName,
            phoneNumber = phoneNumber
        )
        users[email] = newUser
        _currentUser.value = newUser
        return Result.success(newUser)
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
        jwtToken = null
        _currentUser.value = null
    }

    fun getToken(): String? = jwtToken
}

/**
 * LaundryRepository handles API integration for laundry services, bookings, orders, demo payments, and contact messages.
 */
class LaundryRepository {
    private val api = RetrofitClient.apiService

    // Seeded services (11 services)
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

    // Seeded orders (10 orders)
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

    suspend fun fetchServices(): List<LaundryService> {
        return try {
            val response = api.getLaundryServices()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                _services
            }
        } catch (e: Exception) {
            _services
        }
    }

    fun getServices(): List<LaundryService> = _services

    suspend fun createBooking(userId: String, serviceId: Int, address: String, notes: String, token: String?): Result<LaundryOrder> {
        return try {
            val response = api.createBooking(BookingRequest(userId, serviceId, address, notes), token?.let { "Bearer $it" })
            if (response.isSuccessful && response.body() != null) {
                val created = response.body()!!
                _orders.value = listOf(created) + _orders.value
                Result.success(created)
            } else {
                Result.success(createLocalOrder(userId, serviceId, address, notes))
            }
        } catch (e: Exception) {
            Result.success(createLocalOrder(userId, serviceId, address, notes))
        }
    }

    private fun createLocalOrder(userId: String, serviceId: Int, address: String, notes: String): LaundryOrder {
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

    suspend fun submitDemoPayment(orderIdInt: Int, paymentMethod: String): Result<PaymentResponse> {
        return try {
            val response = api.submitDemoPayment(PaymentRequest(orderIdInt, paymentMethod))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.success(PaymentResponse(101, orderIdInt, 150.0, paymentMethod, "TXN-DEMO-" + UUID.randomUUID().toString().take(6), "Completed", true))
            }
        } catch (e: Exception) {
            Result.success(PaymentResponse(101, orderIdInt, 150.0, paymentMethod, "TXN-DEMO-" + UUID.randomUUID().toString().take(6), "Completed", true))
        }
    }

    suspend fun submitContact(fullName: String, email: String, subject: String, message: String): Result<Unit> {
        return try {
            val response = api.submitContactMessage(ContactMessageRequest(fullName, email, subject, message))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.success(Unit)
        }
    }

    suspend fun fetchNotifications(userId: String): List<NotificationDto> {
        return try {
            val response = api.getUserNotifications(userId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

/**
 * SettingsRepository manages user preferences.
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
