package com.example.inandoutlaundry.data.remote

import com.example.inandoutlaundry.data.model.Customer
import com.example.inandoutlaundry.data.model.LaundryOrder
import com.example.inandoutlaundry.data.model.LaundryService
import com.example.inandoutlaundry.data.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Request and Response DTOs for ASP.NET Core Web API integration.
 * Developed by third-year software development student.
 */
data class LoginRequest(val email: String, val passwordHash: String)
data class LoginResponse(val token: String, val user: User)

data class BookingRequest(val userId: String, val serviceId: Int, val deliveryAddress: String, val notes: String)
data class PaymentRequest(val orderId: Int, val paymentMethod: String)
data class PaymentResponse(
    val paymentId: Int,
    val orderId: Int,
    val amount: Double,
    val paymentMethod: String,
    val transactionReference: String,
    val paymentStatus: String,
    val isDemo: Boolean
)

data class ContactMessageRequest(val fullName: String, val email: String, val subject: String, val message: String)
data class NotificationDto(val id: Int, val userId: String, val title: String, val message: String, val isRead: Boolean, val createdAt: String)

/**
 * Retrofit API Service interface matching the ASP.NET Core Web API controllers and endpoints.
 */
interface ApiService {
    // Authentication & Users
    @POST("Users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("Auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>

    // CustomersController endpoints
    @GET("Customers")
    suspend fun getCustomers(@Header("Authorization") token: String? = null): Response<List<Customer>>

    @GET("Customers/{id}")
    suspend fun getCustomer(@Path("id") id: Int, @Header("Authorization") token: String? = null): Response<Customer>

    @POST("Customers")
    suspend fun createCustomer(@Body customer: Customer): Response<Customer>

    @PUT("Customers/{id}")
    suspend fun updateCustomer(@Path("id") id: Int, @Body customer: Customer): Response<Unit>

    @DELETE("Customers/{id}")
    suspend fun deleteCustomer(@Path("id") id: Int): Response<Unit>

    // LaundryServicesController
    @GET("LaundryServices")
    suspend fun getLaundryServices(): Response<List<LaundryService>>

    // BookingsController
    @GET("Bookings")
    suspend fun getBookings(@Header("Authorization") token: String? = null): Response<List<LaundryOrder>>

    @POST("Bookings")
    suspend fun createBooking(@Body request: BookingRequest, @Header("Authorization") token: String? = null): Response<LaundryOrder>

    // OrdersController
    @GET("Orders")
    suspend fun getOrders(@Header("Authorization") token: String? = null): Response<List<LaundryOrder>>

    // OrderTrackingController
    @GET("OrderTracking/order/{orderId}")
    suspend fun getOrderTracking(@Path("orderId") orderId: String): Response<List<Any>>

    @GET("OrderTracking/latest/{orderId}")
    suspend fun getLatestTracking(@Path("orderId") orderId: String): Response<Any>

    // PaymentsController
    @POST("Payments/demo")
    suspend fun submitDemoPayment(@Body request: PaymentRequest): Response<PaymentResponse>

    @GET("Payments/order/{orderId}")
    suspend fun getPaymentsForOrder(@Path("orderId") orderId: Int): Response<List<PaymentResponse>>

    // ContactMessagesController
    @POST("ContactMessages")
    suspend fun submitContactMessage(@Body request: ContactMessageRequest): Response<Unit>

    // NotificationsController
    @GET("Notifications/user/{userId}")
    suspend fun getUserNotifications(@Path("userId") userId: String): Response<List<NotificationDto>>

    @GET("Notifications/unread/{userId}")
    suspend fun getUnreadNotifications(@Path("userId") userId: String): Response<List<NotificationDto>>

    @GET("Notifications/unread-count/{userId}")
    suspend fun getUnreadCount(@Path("userId") userId: String): Response<Int>
}

/**
 * Singleton Retrofit Client configured for development base URL http://10.0.2.2:5062/api/.
 */
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5062/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
