package com.example.jackenpoy.modules.game.ui

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jackenpoy.modules.auth.ui.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorGameScreen(
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

    if (currentGameSession?.rounds == 0) {
        var gameSession = currentGameSession
        var message = "You win!";

        // no more rounds
        if (currentGameSession.creatorWins > currentGameSession.opponentWins) {
            // update the winner id
            gameSession = gameSession.copy(
                winnerId = gameSession.winnerId
            )
        } else {
            // update the winner id
            gameSession = gameSession.copy(
                winnerId = gameSession.opponentId
            )

            message = "You lose! The opponent won!"
        }

        // update the game session
        gameViewModel.updateGameSession(
            gameSession.id.toString(),
            gameSession
        )

        // notify
        Toast.makeText(LocalContext.current, message, Toast.LENGTH_LONG).show()

        // exit the game
        gameViewModel.exitGameSession()
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

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Rock Paper Scissors") })
        }
    ) { paddingValues ->
        // reveal hand state
        var revealHands by rememberSaveable { mutableStateOf(if (currentGameSession == null) false else currentGameSession.showHands) }

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
                    if (revealHands) {
                        when (opposingPlayerHand) {
                            Hand.ROCK -> Text("Rock")
                            Hand.PAPER -> Text("Paper")
                            Hand.SCISSORS -> Text("Scissors")
                            Hand.NONE -> Text("")
                        }
                    }

                    when (creatorPlayerHand) {
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
                // only reveal show hands button if creator
                if (creatorPlayerHand != Hand.NONE && opposingPlayerHand != Hand.NONE) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (!revealHands) {
                                val result = gameViewModel.determineWinner(
                                    creatorPlayerHand,
                                    opposingPlayerHand
                                )

                                // create update game session and reduce the rounds
                                var gameSession = gameState.currentGameSession?.copy(
                                    rounds = gameState.currentGameSession!!.rounds.minus(1)
                                )

                                if (result != null && result) {
                                    // creator wins
                                    gameSession = gameSession?.copy(
                                        creatorWins = gameSession.creatorWins.plus(1)
                                    )
                                } else if (result != null) {
                                    // opposing wins
                                    gameSession = gameSession?.copy(
                                        opponentWins = gameSession.opponentWins.plus(1)
                                    )
                                }

                                // update
                                if (gameSession != null) {
                                    gameSession = gameSession.copy(
                                        showHands = true // show the hands
                                    )

                                    gameViewModel.updateGameSession(
                                        gameSession.id.toString(),
                                        gameSession
                                    )
                                }
                            } else {
                                // reset hands
                                var gameSession = gameState.currentGameSession?.copy(
                                    creatorHand = Hand.NONE.code,
                                    opponentHand = Hand.NONE.code,
                                    showHands = false // hide the hands
                                )

                                // update
                                if (gameSession != null) {
                                    gameViewModel.updateGameSession(
                                        gameSession.id.toString(),
                                        gameSession
                                    )
                                }
                            }

                            revealHands = !revealHands
                        },
                    ) {
                        Text(if (!revealHands) "Reveal Hands" else "Next Round")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                HandButtons(modifier = Modifier.fillMaxWidth()) { it ->
                    val gameSession = gameState.currentGameSession?.copy(
                        creatorHand = it.code
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

