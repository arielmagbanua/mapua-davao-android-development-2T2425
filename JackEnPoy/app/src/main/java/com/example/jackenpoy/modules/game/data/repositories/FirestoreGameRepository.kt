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

        val docSnapshot = docRef.get().await()
        if (docSnapshot.exists()) {
            // convert and serialize to game session
            return docSnapshot.toObject(GameSession::class.java)
        }

        return null
    }

    override fun readGameSession(
        gameId: String,
        onRead: (GameSession?) -> Unit
    ) {
        gameSessions.document(gameId).addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val gameSession = snapshot.toObject(GameSession::class.java)
                onRead(gameSession)
            } else {
                onRead(null)
            }
        }
    }
}
