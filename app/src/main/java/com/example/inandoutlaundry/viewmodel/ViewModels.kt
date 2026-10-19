package com.example.inandoutlaundry.viewmodel

import androidx.lifecycle.ViewModel
import com.example.inandoutlaundry.data.model.LaundryOrder
import com.example.inandoutlaundry.data.model.LaundryService
import com.example.inandoutlaundry.data.model.User
import com.example.inandoutlaundry.data.model.UserPreference
import com.example.inandoutlaundry.data.repository.AuthRepository
import com.example.inandoutlaundry.data.repository.LaundryRepository
import com.example.inandoutlaundry.data.repository.SettingsRepository
import com.example.inandoutlaundry.utils.ValidationUtils
import kotlinx.coroutines.flow.StateFlow

/**
 * AuthViewModel handles authentication business logic, input validation, and user session state.
 * Adheres to MVVM architecture, exposing StateFlow to Jetpack Compose UI.
 */
class AuthViewModel(
    val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    val currentUser: StateFlow<User?> = authRepository.currentUser

    fun login(email: String, pass: String): Result<User> {
        if (!ValidationUtils.isValidEmail(email)) {
            return Result.failure(Exception("Please enter a valid email address."))
        }
        if (pass.isBlank()) {
            return Result.failure(Exception("Password cannot be blank."))
        }
        return authRepository.login(email, pass)
    }

    fun register(email: String, pass: String, confirmPass: String, name: String, phone: String): Result<User> {
        if (!ValidationUtils.isValidName(name)) {
            return Result.failure(Exception("Please enter a valid full name."))
        }
        if (!ValidationUtils.isValidEmail(email)) {
            return Result.failure(Exception("Please enter a valid email address."))
        }
        if (!ValidationUtils.isValidPhone(phone)) {
            return Result.failure(Exception("Please enter a valid phone number (at least 10 digits)."))
        }
        if (!ValidationUtils.isValidPassword(pass)) {
            return Result.failure(Exception("Password must be at least 6 characters and include letters and numbers."))
        }
        if (pass != confirmPass) {
            return Result.failure(Exception("Passwords do not match."))
        }
        return authRepository.register(email, pass, name, phone)
    }

    fun loginWithGoogle(email: String, name: String): User {
        return authRepository.loginWithGoogle(email, name)
    }

    fun logout() {
        authRepository.logout()
    }
}

/**
 * LaundryViewModel manages state and business operations for laundry services and orders.
 */
class LaundryViewModel(
    val laundryRepository: LaundryRepository = LaundryRepository()
) : ViewModel() {
    val services: List<LaundryService> = laundryRepository.getServices()
    val orders: StateFlow<List<LaundryOrder>> = laundryRepository.orders

    fun createOrder(userId: String, serviceId: Int, address: String, notes: String): Result<LaundryOrder> {
        if (address.isBlank()) {
            return Result.failure(Exception("Delivery address is required."))
        }
        val order = laundryRepository.createOrder(userId, serviceId, address, notes)
        return Result.success(order)
    }
}

/**
 * SettingsViewModel manages user preferences state (Dark mode, notifications, address).
 */
class SettingsViewModel(
    val settingsRepository: SettingsRepository = SettingsRepository()
) : ViewModel() {
    val preference: StateFlow<UserPreference> = settingsRepository.preference

    fun updatePreferences(darkMode: Boolean, notificationsEnabled: Boolean, address: String) {
        settingsRepository.updatePreferences(darkMode, notificationsEnabled, address)
    }
}
