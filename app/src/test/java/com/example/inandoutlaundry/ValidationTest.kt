package com.example.inandoutlaundry

import com.example.inandoutlaundry.utils.SecurityUtils
import com.example.inandoutlaundry.utils.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

/**
 * Automated Unit Tests for validation logic and password security hashing.
 * Developed by third-year software development student for academic grading verification.
 */
class ValidationTest {

    @Test
    fun testEmailValidation() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"))
        assertTrue(ValidationUtils.isValidEmail("user.name+tag@sub.domain.co.za"))
        assertFalse(ValidationUtils.isValidEmail("invalid-email"))
        assertFalse(ValidationUtils.isValidEmail(""))
    }

    @Test
    fun testPasswordValidation() {
        assertTrue(ValidationUtils.isValidPassword("Password123"))
        assertFalse(ValidationUtils.isValidPassword("short"))
        assertFalse(ValidationUtils.isValidPassword("abcdefgh"))
        assertFalse(ValidationUtils.isValidPassword("12345678"))
    }

    @Test
    fun testPhoneValidation() {
        assertTrue(ValidationUtils.isValidPhone("0821234567"))
        assertTrue(ValidationUtils.isValidPhone("+27821234567"))
        assertFalse(ValidationUtils.isValidPhone("123"))
    }

    @Test
    fun testPasswordHashing() {
        val hash1 = SecurityUtils.hashPassword("Password123")
        val hash2 = SecurityUtils.hashPassword("Password123")
        val hash3 = SecurityUtils.hashPassword("Different123")

        assertEquals(hash1, hash2)
        assertNotEquals(hash1, hash3)
        assertEquals(64, hash1.length) // SHA-256 hex length verification
    }
}
