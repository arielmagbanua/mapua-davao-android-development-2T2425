package com.example.jackenpoy.modules.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun createGameSession(creatorId: String, onCreateGameSession: ((GameSession?) -> Unit)? = null) {
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

    fun onGameSessionUpdates(gameSession: GameSession, onUpdate: (GameSession) -> Unit) {

    }
}
