package com.example.jackenpoy.modules.game.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.jackenpoy.modules.auth.ui.AuthViewModel

@Composable
fun SessionsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    Scaffold(modifier = modifier.safeContentPadding()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

        }
    }
}