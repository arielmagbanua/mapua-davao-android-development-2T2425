package com.example.jackenpoy.modules.game.domain

import com.example.jackenpoy.modules.game.data.models.GameSession

interface GameServiceInterface {
    suspend fun createGameSession(creatorId: String): GameSession?
}
