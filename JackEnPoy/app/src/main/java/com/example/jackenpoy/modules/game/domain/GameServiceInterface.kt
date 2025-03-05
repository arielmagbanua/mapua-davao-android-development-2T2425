package com.example.jackenpoy.modules.game.domain

import com.example.jackenpoy.modules.game.data.models.GameSession

interface GameServiceInterface {
    suspend fun createGameSession(creatorId: String): GameSession?

    fun readGameSession(gameId: String, readOnce: Boolean = false, onRead: (GameSession?) -> Unit)

    fun updateGameSession(gameId: String, updated: GameSession)

    fun readOpenGameSessions(currentUserId: String, onRead: (List<GameSession>) -> Unit)
}
