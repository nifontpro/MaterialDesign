package ru.nifontbus.materialdesign.ui.recycler.notes.room

import ru.nifontbus.materialdesign.ui.recycler.notes.Note

interface LocalRepository {
    fun getAllNotes(): List<Note>
//    fun saveNote(note: Note): Long
    fun saveNote(note: Note)
    fun deleteById(id: Long)
}