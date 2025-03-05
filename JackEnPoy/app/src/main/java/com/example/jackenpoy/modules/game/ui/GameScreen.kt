package com.example.jackenpoy.modules.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jackenpoy.modules.auth.ui.AuthViewModel

enum class Choice(val code: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3),
    NONE(0);

    companion object {
        private val map = Choice.entries.associateBy(Choice::code)

        fun fromCode(code: Int): Choice? = map[code]
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel()
) {
    val currentUser = authViewModel.getCurrentUser()
    val gameState by gameViewModel.gameState.collectAsState()
    val currentGameSession = gameState.currentGameSession

    var currentPlayerChoice by rememberSaveable {
        mutableStateOf(
            if (currentGameSession == null) Choice.NONE else Choice.fromCode(
                currentGameSession.creatorHand
            ) ?: Choice.NONE
        )
    }
    var opposingPlayerChoice by rememberSaveable {
        mutableStateOf(
            if (currentGameSession == null) Choice.NONE else Choice.fromCode(
                currentGameSession.opponentHand
            ) ?: Choice.NONE
        )
    }

    // setup game updates
    LaunchedEffect(Unit) {
        gameViewModel.subscribeToSessionUpdates(
            authViewModel.getCurrentUser()?.id.toString(),
            gameState.currentGameSession?.id.toString()
        ) { updatedSession ->

            currentPlayerChoice = if (updatedSession == null) Choice.NONE else Choice.fromCode(
                updatedSession.creatorHand
            ) ?: Choice.NONE

            opposingPlayerChoice = if (updatedSession == null) Choice.NONE else Choice.fromCode(
                updatedSession.opponentHand
            ) ?: Choice.NONE
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Rock Paper Scissors") })
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
                    Text("Player")

                    when (currentPlayerChoice) {
                        Choice.ROCK -> Text("Rock")
                        Choice.PAPER -> Text("Paper")
                        Choice.SCISSORS -> Text("Scissors")
                        Choice.NONE -> Text("")
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // only reveal show hands button if creator
                if (currentUser?.id == currentGameSession?.creatorId) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val gameSession = gameState.currentGameSession?.copy(
                                creatorHand = currentPlayerChoice.code
                            )
                            if (gameSession != null) {
                                gameViewModel.updateGameSession(
                                    gameSession.id.toString(),
                                    gameSession
                                )
                            }
                        },
                    ) {
                        Text("Show Hands")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                ChoiceButtons(modifier = Modifier.fillMaxWidth()) {
                    it -> currentPlayerChoice = it
                }
            }
        }
    }
}

@Composable
fun ChoiceButtons(
    modifier: Modifier = Modifier,
    onChoiceSelected: (Choice) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
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
