package com.example.inandoutlaundry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.inandoutlaundry.ui.navigation.NavGraph
import com.example.inandoutlaundry.ui.theme.InAndOutLaundryTheme
import com.example.inandoutlaundry.viewmodel.AuthViewModel
import com.example.inandoutlaundry.viewmodel.LaundryViewModel
import com.example.inandoutlaundry.viewmodel.SettingsViewModel

/**
 * MainActivity serves as the single entry point for the In and Out Laundry Android application.
 * Developed for OPSC6312 by a third-year IT Software Development student.
 * Responsibilities:
 * - Enables edge-to-edge display rendering.
 * - Instantiates MVVM ViewModels to maintain state across configuration changes.
 * - Sets up the Jetpack Compose Material 3 theme and Navigation graph host.
 */
class MainActivity : ComponentActivity() {
    
    // ViewModels initialized at the Activity level to retain UI state across navigation and screen rotations
    private val authViewModel = AuthViewModel()
    private val laundryViewModel = LaundryViewModel()
    private val settingsViewModel = SettingsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display for modern Android UI guidelines
        enableEdgeToEdge()
        setContent {
            InAndOutLaundryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialize the central Navigation Graph for screen transitions
                    NavGraph(
                        authViewModel = authViewModel,
                        laundryViewModel = laundryViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
