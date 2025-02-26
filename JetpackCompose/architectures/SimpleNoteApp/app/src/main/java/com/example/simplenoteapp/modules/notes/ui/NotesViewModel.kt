package com.example.simplenoteapp.modules.notes.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class NotesViewModel : ViewModel() {
    fun addNote(note: HashMap<String, String>) {
        val db = Firebase.firestore

        db.collection("notes").add(note)
            .addOnSuccessListener { docRef ->
                Log.d("NOTE", "id: ${docRef.id}")
            }
    }

    fun getNotes(owner: String, onRead: (notes: List<HashMap<String, Any?>>) -> Unit) {
        val db = Firebase.firestore

        val docRef = db.collection("notes")
            .whereEqualTo("owner", owner)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("NOTE", "Listen failed.")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val documents = snapshot.documents

                val notes = documents.map { docSnapshot ->
                    hashMapOf(
                        "id" to docSnapshot.id,
                        "title" to docSnapshot.get("title"),
                        "content" to docSnapshot.get("content"),
                        "owner" to docSnapshot.get("owner")
                    )
                }

                onRead(notes)
                Log.d("NOTE", "Current data: ${documents.size}")
            }
        }
    }

    fun deleteNote(id: String) {
        val db = Firebase.firestore

        db.collection("notes").document(id).delete().addOnCompleteListener {
            Log.d("NOTE", "Note deleted")
        }
    }

    suspend fun readNote(id: String): HashMap<String, Any?>? {
        val db = Firebase.firestore

        val docSnapshot = db.collection("notes").document(id).get().await()

        if (docSnapshot.exists()) {
            // extract notes
            return hashMapOf<String, Any?>(
                "title" to docSnapshot.get("title"),
                "content" to docSnapshot.get("content"),
                "owner" to docSnapshot.get("owner")
            )
        }

        return null
    }

    fun updateNote(id: String, note: HashMap<String, String>) {
        val db = Firebase.firestore

        db.collection("notes").document(id).update(note.toMap())
    }
}