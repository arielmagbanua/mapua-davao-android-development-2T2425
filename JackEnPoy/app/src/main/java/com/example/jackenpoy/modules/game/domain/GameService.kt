package com.example.jackenpoy.modules.game.domain

import com.example.jackenpoy.modules.game.data.models.GameSession
import com.example.jackenpoy.modules.game.data.repositories.GameRepositoryInterface
import javax.inject.Inject

class GameService @Inject constructor(
    private val gameRepository: GameRepositoryInterface
) : GameServiceInterface {
    override suspend fun createGameSession(creatorId: String): GameSession? {
        return gameRepository.createGameSession(creatorId)
    }

    override fun readGameSession(
        gameId: String,
        readOnce: Boolean,
        onRead: (GameSession?) -> Unit
    ) {
        gameRepository.readGameSession(gameId, readOnce, onRead)
    }

    override fun updateGameSession(
        gameId: String,
        updated: GameSession
    ) {
        gameRepository.updateGameSession(gameId, updated)
    }

    override fun readOpenGameSessions(currentUserId: String, onRead: (List<GameSession>) -> Unit) {
        gameRepository.readOpenGameSessions(currentUserId, onRead)
    }
}
