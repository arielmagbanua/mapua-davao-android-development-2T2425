package com.example.jackenpoy.modules.game.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jackenpoy.R
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import com.example.jackenpoy.modules.game.data.models.GameSession


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel()
) {
    // get the current user
    val currentUser = authViewModel.getCurrentUser()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.available_match))
                },
                actions = {
                    IconButton(onClick = {
                        // logout the user
                        authViewModel.logout()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.logout)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // create a new game
                gameViewModel.createGameSession(currentUser?.id.toString()) { gameSession ->
                    if (gameSession != null) {
                        // join the game
                        gameViewModel.joinGameSession(gameSession)
                        Log.d("SessionsScreen", "Game session created with id: ${gameSession.id}")
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = stringResource(R.string.create_match)
                )
            }
        }
    ) { innerPadding ->
        var availableSessions by rememberSaveable { mutableStateOf(listOf<GameSession>()) }

        LaunchedEffect(Unit) {
            // read updates of all sessions here
            gameViewModel.readOpenGameSessions(currentUser?.id.toString()) { sessions ->
                availableSessions = sessions
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(
                availableSessions.size,
                key = { index -> availableSessions[index].id.toString() }
            ) { index ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = availableSessions[index].creatorDisplayName.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Rounds: " + availableSessions[index].rounds.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingContent = {
                        if (currentUser != null) {
                            Button(
                                onClick = {
                                    // join as opponent
                                    gameViewModel.joinGameSession(
                                        gameId = availableSessions[index].id.toString(),
                                        opponent = currentUser
                                    )
                                },
                            ) { Text("Join") }
                        }
                    }
                )
            }
        }
    }
}