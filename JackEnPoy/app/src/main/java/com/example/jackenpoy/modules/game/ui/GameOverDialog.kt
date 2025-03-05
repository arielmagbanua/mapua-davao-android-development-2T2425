package com.example.jackenpoy.modules.game.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameOverDialog(message: String, onDismissRequest: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest, // Dismiss when clicked outside
        title = { Text("No More Rounds!") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("OK")
            }
        },
    )
}
