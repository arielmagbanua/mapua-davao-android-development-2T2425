package com.example.simplenoteapp.modules.notes.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class NotesViewModel: ViewModel() {
    fun addNote(note: HashMap<String, String>) {
        val db = Firebase.firestore

        db.collection("notes").add(note)
            .addOnSuccessListener { docRef ->
                Log.d("NOTE", "id: ${docRef.id}")
                // Log.d("NOTE", "title: ${docRef.get().result.get("title")}")
                // Log.d("NOTE", "content: ${docRef.get().result.get("content")}")
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
}