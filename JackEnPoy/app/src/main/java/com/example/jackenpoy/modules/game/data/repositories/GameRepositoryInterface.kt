package com.example.jackenpoy.modules.game.data.repositories

import com.example.jackenpoy.modules.game.data.models.GameSession

interface GameRepositoryInterface {
    suspend fun createGameSession(creatorId: String, creatorDisplayName: String? = null): GameSession?

    fun readGameSession(gameId: String, readOnce: Boolean = false, onRead: (GameSession?) -> Unit)

    fun updateGameSession(gameId: String, updated: GameSession)

    fun readOpenGameSessions(currentUserId: String, onRead: (List<GameSession>) -> Unit)
}
