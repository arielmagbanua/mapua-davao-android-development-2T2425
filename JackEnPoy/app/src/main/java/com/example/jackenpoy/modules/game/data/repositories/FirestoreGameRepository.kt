package com.example.jackenpoy.modules.game.data.repositories

import com.example.jackenpoy.modules.game.data.models.GameSession
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreGameRepository : GameRepositoryInterface {
    private val db = Firebase.firestore
    private val gameSessions = db.collection("sessions")

    override suspend fun createGameSession(creatorId: String): GameSession? {
        // create game session
        val gameSession = GameSession(creatorId = creatorId)
        val docRef = gameSessions.add(gameSession).await()

        val snapshot = docRef.get().await()
        if (snapshot.exists()) {
            // convert and serialize to game session
            var gameSession = snapshot.toObject(GameSession::class.java)
            gameSession = gameSession?.copy(id = snapshot.id)

            return gameSession
        }

        return null
    }

    override fun readGameSession(
        gameId: String,
        onRead: (GameSession?) -> Unit
    ) {
        gameSessions.document(gameId).addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                var gameSession = snapshot.toObject(GameSession::class.java)
                gameSession = gameSession?.copy(id = snapshot.id)

                onRead(gameSession)
            } else {
                onRead(null)
            }
        }
    }

    override fun updateGameSession(
        gameId: String,
        updated: GameSession
    ) {
        gameSessions.document(gameId).set(updated.toMap())
    }

    override fun readOpenGameSessions(onRead: (List<GameSession>) -> Unit) {
        gameSessions.whereEqualTo("creatorId", null).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val sessionDocs = snapshot.documents
                var sessions = emptyList<GameSession>()

                if (sessionDocs.isEmpty()) {
                    onRead(sessions)
                    return@addSnapshotListener
                }

                sessions = sessionDocs.map {
                    var gameSession = it.toObject(GameSession::class.java)
                    gameSession = gameSession?.copy(id = it.id)
                    gameSession as GameSession
                }

                onRead(sessions)
            }
        }
    }
}
