package com.example.inandoutlaundry

import com.example.inandoutlaundry.data.repository.LaundryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Automated Unit Tests for LaundryRepository verifying data seeding requirements (>= 10 records per table).
 */
class LaundryRepositoryTest {

    @Test
    fun testServicesSeededCount() {
        val repo = LaundryRepository()
        val services = repo.getServices()
        assertTrue("Services should have at least 10 records", services.size >= 10)
    }

    @Test
    fun testOrdersSeededCount() {
        val repo = LaundryRepository()
        val orders = repo.orders.value
        assertTrue("Orders should have at least 10 records", orders.size >= 10)
    }

    @Test
    fun testCreateBooking() = runBlocking {
        val repo = LaundryRepository()
        val initialSize = repo.orders.value.size
        val result = repo.createBooking("user_1", 1, "124 Main Road", "Fragrance free", null)
        assertTrue(result.isSuccess)
        assertEquals(initialSize + 1, repo.orders.value.size)
        assertEquals("Standard Wash & Fold", result.getOrNull()?.serviceName)
        assertEquals("Received", result.getOrNull()?.status)
    }
}
