package com.example.jackenpoy.modules.game.data.models

data class GameSession(
    val id: String? = null,
    val creatorId: String? = null,
    val creatorHand: Int? = null,
    val opponentId: String? = null,
    val opponentHand: Int? = null,
    val winnerId: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "creatorId" to creatorId,
            "creatorHand" to creatorHand,
            "opponentId" to opponentId,
            "opponentHand" to opponentHand,
            "winnerId" to winnerId
        )
    }
}
