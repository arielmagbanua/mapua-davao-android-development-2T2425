package com.example.simplenoteapp.modules.notes.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirestoreRepository : DataRepositoryInterface {
    private val db = Firebase.firestore

    override fun addNote(
        note: Note,
        onAdd: ((Boolean) -> Unit)?
    ) {
        db.collection("notes").add(note)
            .addOnSuccessListener { docRef ->
                Log.d("NOTE", "id: ${docRef.id}")
                onAdd?.invoke(true)
            }
            .addOnFailureListener {
                onAdd?.invoke(false)
            }
    }

    override fun getNotes(
        owner: String,
        onRead: (List<Note>) -> Unit
    ) {
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
                    Note(
                        id = docSnapshot.id,
                        title = docSnapshot.get("title") as String,
                        content = docSnapshot.get("content") as String,
                        owner = docSnapshot.get("owner") as String
                    )
                }

                onRead(notes)
                Log.d("NOTE", "Current data: ${documents.size}")
            }
        }
    }

    override fun deleteNote(id: String, onDelete: ((Boolean) -> Unit)?) {
        db.collection("notes").document(id).delete().addOnCompleteListener { task ->
            onDelete?.invoke(task.isSuccessful)
        }
    }

    override fun readNote(
        id: String,
        onRead: (Note?) -> Unit
    ) {
        db.collection("notes").document(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { docSnapshot ->
                    if (docSnapshot.exists()) {
                        val note = Note(
                            id = docSnapshot.id,
                            title = docSnapshot.get("title") as String,
                            content = docSnapshot.get("content") as String,
                            owner = docSnapshot.get("owner") as String
                        )

                        onRead(note)
                        return@let
                    }

                    onRead(null)
                }

                return@addOnCompleteListener
            }

            onRead(null)
        }
    }

    override fun updateNote(
        id: String,
        note: Note,
        onUpdate: ((Boolean) -> Unit)?
    ) {
        db.collection("notes").document(id).update(note.toMap()).addOnCompleteListener { task ->
            onUpdate?.invoke(task.isSuccessful)
        }
    }
}