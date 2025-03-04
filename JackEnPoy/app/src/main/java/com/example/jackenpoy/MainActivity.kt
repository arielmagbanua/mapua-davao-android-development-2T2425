package com.example.jackenpoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jackenpoy.modules.auth.data.FirebaseAuthRepository
import com.example.jackenpoy.modules.auth.domain.AuthService
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import com.example.jackenpoy.modules.auth.ui.LoginScreen
import com.example.jackenpoy.modules.game.ui.SessionsScreen
import com.example.jackenpoy.ui.theme.JackEnPoyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel =
            AuthViewModel(authService = AuthService(authRepository = FirebaseAuthRepository()))

        enableEdgeToEdge()
        setContent {
            JackEnPoyTheme {
                val authState by authViewModel.authState.collectAsState()

                val startDestination = if (authState.currentUser != null) "sessions" else "login"
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable("login") {
                        LoginScreen(authViewModel = authViewModel, navController = navController)
                    }

                    composable("sessions") {
                        SessionsScreen(authViewModel = authViewModel, navController = navController)
                    }
                }
            }
        }
    }
}
