package com.example.simplenoteapp.modules.notes.data

interface DataRepositoryInterface {
    fun addNote(note: Note, onAdd: ((successful: Boolean) -> Unit)? = null)

    fun getNotes(owner: String, onRead: (notes: List<Note>) -> Unit)

    fun deleteNote(id: String, onDelete: ((successful: Boolean) -> Unit)? = null)

    fun readNote(id: String, onRead: (note: Note?) -> Unit)

    fun updateNote(id: String, note: Note, onUpdate: ((successful: Boolean) -> Unit)? = null)
}
