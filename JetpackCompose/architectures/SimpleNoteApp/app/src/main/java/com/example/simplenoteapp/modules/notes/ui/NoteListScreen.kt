package com.example.simplenoteapp.modules.notes.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.simplenoteapp.modules.auth.ui.AuthViewModel

@Composable
fun NoteListScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavController) {
    Column (modifier = modifier) {
        Text(text = "Note List Screen")
        Button(onClick = {
            authViewModel.logout()
        }) {
            Text(text = "Logout")
        }

        Button(onClick = {
            navController.navigate("registration")
        }) {
            Text(text = "Registration")
        }
    }
}