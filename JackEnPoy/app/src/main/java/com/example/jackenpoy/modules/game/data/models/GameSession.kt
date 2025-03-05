package com.example.jackenpoy.modules.game.data.models

data class GameSession(
    val id: String? = null,
    val creatorId: String? = null,
    val creatorHand: Int = 0,
    val creatorDisplayName: String? = null,
    val opponentId: String? = null,
    val opponentHand: Int = 0,
    val opponentDisplayName: String? = null,
    val winnerId: String? = null,
    val rounds: Int = 5,
    val creatorWins: Int = 0,
    val opponentWins: Int = 0,
    val showHands: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "creatorId" to creatorId,
            "creatorHand" to creatorHand,
            "creatorDisplayName" to creatorDisplayName,
            "opponentId" to opponentId,
            "opponentHand" to opponentHand,
            "opponentDisplayName" to opponentDisplayName,
            "winnerId" to winnerId,
            "rounds" to rounds,
            "creatorWins" to creatorWins,
            "opponentWins" to opponentWins,
            "showHands" to showHands
        )
    }
}
