package com.example.jackenpoy.modules.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jackenpoy.modules.auth.data.models.User
import com.example.jackenpoy.modules.game.data.models.GameSession
import com.example.jackenpoy.modules.game.domain.GameServiceInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameService: GameServiceInterface
) : ViewModel() {
    // game state
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun createGameSession(
        creatorId: String,
        onCreateGameSession: ((GameSession?) -> Unit)? = null
    ) {
        // view model scope
        viewModelScope.launch {
            val gameSession = gameService.createGameSession(creatorId)
            onCreateGameSession?.invoke(gameSession)
        }
    }

    fun joinGameSession(gameSession: GameSession) {
        // update the game state
        _gameState.update { currentState ->
            currentState.copy(
                currentGameSession = gameSession
            )
        }
    }

    // join game session as opponent
    fun joinGameSession(gameId: String, opponent: User) {
        gameService.readGameSession(gameId = gameId, readOnce = true) { gameSession ->
            if (gameSession != null) {

                // update the game session
                val updatedGameSession = gameSession.copy(
                    opponentId = opponent.id,
                    opponentDisplayName = opponent.name,
                )
                gameService.updateGameSession(gameId, updatedGameSession)

                // update the game state
                _gameState.update { currentState ->
                    currentState.copy(
                        currentGameSession = updatedGameSession
                    )
                }
            }
        }
    }

    fun subscribeToSessionUpdates(gameId: String, onUpdate: ((GameSession?) -> Unit)? = null) {
        gameService.readGameSession(gameId = gameId, readOnce = false) { gameSession ->
            onUpdate?.invoke(gameSession)

            if (gameSession != null) {
                // update the game state'
                _gameState.update { currentState ->
                    currentState.copy(
                        currentGameSession = gameSession
                    )
                }
            }
        }
    }

    fun updateGameSession(gameId: String, updated: GameSession) {
        gameService.updateGameSession(gameId, updated)
    }

    fun readOpenGameSessions(currentUserId: String, onRead: (List<GameSession>) -> Unit) {
        gameService.readOpenGameSessions(currentUserId, onRead)
    }
}
