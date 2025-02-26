package com.example.simplenoteapp.modules.notes.ui

import androidx.lifecycle.ViewModel
import com.example.simplenoteapp.modules.notes.data.DataRepositoryInterface
import com.example.simplenoteapp.modules.notes.data.Note

class NotesViewModel(private val notesRepository: DataRepositoryInterface) : ViewModel() {
    fun addNote(note: Note, onAdd: ((successful: Boolean) -> Unit)? = null) {
        notesRepository.addNote(note) { success ->
            onAdd?.invoke(success)
        }
    }

    fun getNotes(owner: String, onRead: (notes: List<Note>) -> Unit) {
        notesRepository.getNotes(owner) { notes ->
            onRead(notes)
        }
    }

    fun deleteNote(id: String, onDelete: ((successful: Boolean) -> Unit)? = null) {
        notesRepository.deleteNote(id) { success ->
            onDelete?.invoke(success)
        }
    }

    fun readNote(id: String, onRead: (note: Note?) -> Unit) {
        notesRepository.readNote(id) { note ->
            onRead(note)
        }
    }

    fun updateNote(id: String, note: Note, onUpdate: ((successful: Boolean) -> Unit)? = null) {
        notesRepository.updateNote(id, note) { success ->
            onUpdate?.invoke(success)
        }
    }
}