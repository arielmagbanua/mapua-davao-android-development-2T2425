package com.example.jackenpoy.modules.game.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jackenpoy.modules.auth.ui.AuthViewModel

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel()
) {
    val gameState by gameViewModel.gameState.collectAsState()
    var gameSession by rememberSaveable { mutableStateOf(gameState.currentGameSession) }

    // listen for live updates of the game
    if (gameSession != null) {
        gameViewModel.onGameSessionUpdates(gameSession!!) {
            gameSession = it
        }
    }


    Column {

    }
}