package com.example.simplenoteapp.modules.notes.data

data class Note(val id: String? = null, val title: String, val content: String, val owner: String) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "content" to content,
            "owner" to owner
        )
    }
}
