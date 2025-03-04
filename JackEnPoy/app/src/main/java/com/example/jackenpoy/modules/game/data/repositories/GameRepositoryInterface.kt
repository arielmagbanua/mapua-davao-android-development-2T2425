package com.example.jackenpoy.modules.game.data.repositories

import com.example.jackenpoy.modules.game.data.models.GameSession

interface GameRepositoryInterface {
    suspend fun createGameSession(creatorId: String): GameSession?
}
