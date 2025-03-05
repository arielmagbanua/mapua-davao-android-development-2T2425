package com.example.jackenpoy.modules.game.data.models

data class GameSession(
    val id: String? = null,
    val creatorId: String? = null,
    val creatorHand: Int = 0,
    val opponentId: String? = null,
    val opponentHand: Int = 0,
    val winnerId: String? = null,
    val rounds: Int = 5,
    val creatorWins: Int = 0,
    val opponentWins: Int = 0
) {
    fun toMap(withId: Boolean = false): Map<String, Any?> {
        if (withId) {
            return mapOf(
                "id" to id,
                "creatorId" to creatorId,
                "creatorHand" to creatorHand,
                "opponentId" to opponentId,
                "opponentHand" to opponentHand,
                "winnerId" to winnerId,
                "rounds" to rounds,
                "creatorWins" to creatorWins,
                "opponentWins" to opponentWins
            )
        }

        return mapOf(
            "creatorId" to creatorId,
            "creatorHand" to creatorHand,
            "opponentId" to opponentId,
            "opponentHand" to opponentHand,
            "winnerId" to winnerId,
            "rounds" to rounds,
            "creatorWins" to creatorWins,
            "opponentWins" to opponentWins
        )
    }
}
