package com.example.simplenoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplenoteapp.modules.auth.data.AuthRepository
import com.example.simplenoteapp.modules.auth.ui.AuthViewModel
import com.example.simplenoteapp.modules.auth.ui.LoginScreen
import com.example.simplenoteapp.modules.auth.ui.RegistrationScreen
import com.example.simplenoteapp.modules.notes.data.FirestoreRepository
import com.example.simplenoteapp.modules.notes.ui.NoteScreen
import com.example.simplenoteapp.modules.notes.ui.NoteListScreen
import com.example.simplenoteapp.modules.notes.ui.NotesViewModel
import com.example.simplenoteapp.ui.theme.SimpleNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val notesViewModel: NotesViewModel = hiltViewModel()

            SimpleNoteAppTheme {
                val authState by authViewModel.uiState.collectAsState()
                val authEmail = authState.email

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (authEmail.isNullOrEmpty()) "login" else "notes-list"
                ) {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                        )
                    }

                    composable("notes-list") {
                        NoteListScreen(
                            navController = navController,
                        )
                    }

                    composable("registration") {
                        RegistrationScreen(
                            navController = navController
                        )
                    }

                    composable("note/{id}") { backStackEntry ->
                        val id =backStackEntry.arguments?.getString("id")

                        NoteScreen(
                            navController = navController,
                            id = id
                        )
                    }

                    composable("note") { backStackEntry ->
                        NoteScreen(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    SimpleNoteAppTheme {
        // LoginScreen()
    }
}