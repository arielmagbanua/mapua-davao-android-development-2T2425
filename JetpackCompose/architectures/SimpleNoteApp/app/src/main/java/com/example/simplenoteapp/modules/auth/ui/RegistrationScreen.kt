package com.example.simplenoteapp.modules.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    Column(modifier = modifier) {
        Text(text = "Registration Screen")
        Button(onClick = {
            // pop backstack to previous composable
            navController.popBackStack()
        }) {
            Text(text = "Navigate to Notes")
        }
    }
}