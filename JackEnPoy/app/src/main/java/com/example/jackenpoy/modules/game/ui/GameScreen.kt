package com.example.jackenpoy.modules.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jackenpoy.modules.auth.ui.AuthViewModel

enum class Choice { ROCK, PAPER, SCISSORS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel()
) {
    val gameState by gameViewModel.gameState.collectAsState()
    gameViewModel.subscribeToSessionUpdates(gameState.currentGameSession?.id.toString());

    var player1Choice by rememberSaveable { mutableStateOf(Choice.ROCK) }
    var player2Choice by rememberSaveable { mutableStateOf(Choice.ROCK) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rock Paper Scissors") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Player 1: ${player1Choice.name}")
            Spacer(modifier = Modifier.height(8.dp))
            ChoiceButtons { choice -> player1Choice = choice }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Player 2: ${player2Choice.name}")
            Spacer(modifier = Modifier.height(8.dp))
            ChoiceButtons { choice -> player2Choice = choice }
        }
    }
}

@Composable
fun ChoiceButtons(onChoiceSelected: (Choice) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onChoiceSelected(Choice.ROCK) }) {
            Text("Rock")
        }
        Button(onClick = { onChoiceSelected(Choice.PAPER) }) {
            Text("Paper")
        }
        Button(onClick = { onChoiceSelected(Choice.SCISSORS) }) {
            Text("Scissors")
        }
    }
}