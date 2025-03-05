package com.example.jackenpoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import com.example.jackenpoy.modules.auth.ui.LoginScreen
import com.example.jackenpoy.modules.game.ui.CreatorGameScreen
import com.example.jackenpoy.modules.game.ui.GameViewModel
import com.example.jackenpoy.modules.game.ui.SessionsScreen
import com.example.jackenpoy.ui.theme.JackEnPoyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JackEnPoyTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val gameViewModel: GameViewModel = hiltViewModel()

                val authState by authViewModel.authState.collectAsState()
                val gameState by gameViewModel.gameState.collectAsState()

                var startDestination = "sessions"
                if (authState.currentUser == null) {
                    startDestination = "login"
                } else if (gameState.currentGameSession != null) {
                    // user has current joined the game
                    startDestination = "game"
                }

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }

                    composable("sessions") {
                        SessionsScreen(navController = navController)
                    }

                    composable("game") {
                        if (gameState.currentGameSession?.creatorId == authState.currentUser?.id) {
                            CreatorGameScreen()
                        } else {

                        }
                    }
                }
            }
        }
    }
}
