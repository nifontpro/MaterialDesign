package ru.nifontbus.materialdesign.ui.recycler.notes.room

interface LocalRepository {
    fun getAllNotes(): List<NoteEntity>
    fun saveNote(noteEntity: NoteEntity): Long
    fun deleteById(id: Long)
}