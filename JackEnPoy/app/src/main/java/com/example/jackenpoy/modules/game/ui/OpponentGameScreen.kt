package com.example.jackenpoy.modules.game.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import com.example.jackenpoy.modules.game.data.models.GameSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpponentGameScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel()
) {
    val gameState by gameViewModel.gameState.collectAsState()
    val currentGameSession = gameState.currentGameSession

    var creatorPlayerHand by rememberSaveable {
        mutableStateOf(
            if (currentGameSession == null) Hand.NONE else Hand.fromCode(
                currentGameSession.creatorHand
            ) ?: Hand.NONE
        )
    }
    var opposingPlayerHand by rememberSaveable {
        mutableStateOf(
            if (currentGameSession == null) Hand.NONE else Hand.fromCode(
                currentGameSession.opponentHand
            ) ?: Hand.NONE
        )
    }

    // setup game updates
    LaunchedEffect(Unit) {
        gameViewModel.subscribeToSessionUpdates(
            gameState.currentGameSession?.id.toString()
        ) { updatedSession ->
            // update the creator player hand
            creatorPlayerHand = if (updatedSession == null) Hand.NONE else Hand.fromCode(
                updatedSession.creatorHand
            ) ?: Hand.NONE

            // update the opposing player hand
            opposingPlayerHand = if (updatedSession == null) Hand.NONE else Hand.fromCode(
                updatedSession.opponentHand
            ) ?: Hand.NONE
        }
    }

    if (currentGameSession?.rounds == 0) {
        // no more rounds
        var message = "You win!";

        if (currentGameSession.winnerId != authViewModel.getCurrentUser()?.id) {
            message = "You lost!";
        }

        // notify
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_LONG).show()

        // exit the game
        gameViewModel.exitGameSession()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("You Are Opponent") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (showHands(currentGameSession)) {
                        when (creatorPlayerHand) {
                            Hand.ROCK -> Text("Rock")
                            Hand.PAPER -> Text("Paper")
                            Hand.SCISSORS -> Text("Scissors")
                            Hand.NONE -> Text("")
                        }
                    }

                    when (opposingPlayerHand) {
                        Hand.ROCK -> Text("Rock")
                        Hand.PAPER -> Text("Paper")
                        Hand.SCISSORS -> Text("Scissors")
                        Hand.NONE -> Text("")
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HandButtons(modifier = Modifier.fillMaxWidth()) { it ->
                    val gameSession = gameState.currentGameSession?.copy(
                        opponentHand = it.code
                    )

                    if (gameSession != null) {
                        gameViewModel.updateGameSession(
                            gameSession.id.toString(),
                            gameSession
                        )
                    }
                }
            }
        }
    }
}

// helper method to check if we should show hands
@Composable
fun showHands(gameSession: GameSession?): Boolean {
    if (gameSession == null) {
        return false
    }

    return gameSession.showHands
}