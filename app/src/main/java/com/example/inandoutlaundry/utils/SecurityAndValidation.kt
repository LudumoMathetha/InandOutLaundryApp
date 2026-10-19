package com.example.inandoutlaundry.utils

import java.security.MessageDigest

/**
 * SecurityUtils handles cryptographic operations such as secure password hashing.
 * Requirement: Never store plaintext passwords in the app or database.
 */
object SecurityUtils {
    /**
     * Hashes a plaintext password using SHA-256 before storage or validation comparison.
     */
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

/**
 * ValidationUtils provides rigorous client-side input validation for user forms.
 * Prevents invalid data submission and runtime crashes.
 */
object ValidationUtils {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    /** Validates email format using standard regex matching. */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && EMAIL_REGEX.matches(email)
    }

    /** Validates password complexity: minimum 6 chars, at least one letter and one digit. */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 && password.any { it.isLetter() } && password.any { it.isDigit() }
    }

    /** Validates South African or international phone numbers. */
    fun isValidPhone(phone: String): Boolean {
        return phone.isNotBlank() && phone.length >= 10 && phone.all { it.isDigit() || it == '+' || it == ' ' || it == '-' }
    }

    /** Validates non-empty names with minimum length. */
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
}
