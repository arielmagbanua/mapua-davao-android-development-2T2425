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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplenoteapp.modules.auth.ui.AuthViewModel
import com.example.simplenoteapp.modules.auth.ui.LoginScreen
import com.example.simplenoteapp.modules.auth.ui.RegistrationScreen
import com.example.simplenoteapp.modules.notes.ui.NoteScreen
import com.example.simplenoteapp.modules.notes.ui.NoteListScreen
import com.example.simplenoteapp.modules.notes.ui.NotesViewModel
import com.example.simplenoteapp.ui.theme.SimpleNoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel by viewModels()
            val notesViewModel: NotesViewModel by viewModels()

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
                            authViewModel = authViewModel
                        )
                    }

                    composable("notes-list") {
                        NoteListScreen(
                            authViewModel = authViewModel,
                            navController = navController,
                            notesViewModel = notesViewModel
                        )
                    }

                    composable("registration") {
                        RegistrationScreen(
                            authViewModel = authViewModel,
                            navController = navController
                        )
                    }

                    composable("note") {
                        NoteScreen(
                            authViewModel = authViewModel,
                            navController = navController,
                            notesViewModel = notesViewModel
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