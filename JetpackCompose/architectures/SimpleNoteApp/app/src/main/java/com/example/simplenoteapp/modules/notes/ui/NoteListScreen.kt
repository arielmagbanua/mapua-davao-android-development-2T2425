package com.example.simplenoteapp.modules.notes.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.simplenoteapp.modules.auth.ui.AuthViewModel
import com.example.simplenoteapp.modules.notes.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    notesViewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Notes")
                },
                actions = {
                    IconButton(onClick = {
                        // logout the user
                        authViewModel.logout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // open add notes screen here
                    navController.navigate("note")
                }
            ) {
                Icon(Icons.Filled.Add, "Add a Note")
            }
        }
    ) { innerPadding ->
        val authState by authViewModel.uiState.collectAsState()
        var updatedNotes by remember { mutableStateOf(listOf<Note>()) }

        notesViewModel.getNotes(authState.email.toString()) { notes ->
            updatedNotes = notes
        }

        ResponsiveLazyGrid(
            modifier = Modifier.padding(innerPadding),
            notes = updatedNotes,
            notesViewModel = notesViewModel,
            navController = navController
        )
    }
}