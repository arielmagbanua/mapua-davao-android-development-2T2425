package com.example.jackenpoy.modules.game.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jackenpoy.modules.game.data.models.GameSession
import com.example.jackenpoy.modules.game.domain.GameServiceInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameService: GameServiceInterface
) : ViewModel() {
    fun createGameSession(creatorId: String, onCreateGameSession: (GameSession?) -> Unit) {
        // view model scope
        viewModelScope.launch {
            val gameSession = gameService.createGameSession(creatorId)
            onCreateGameSession(gameSession)
        }
    }
}
