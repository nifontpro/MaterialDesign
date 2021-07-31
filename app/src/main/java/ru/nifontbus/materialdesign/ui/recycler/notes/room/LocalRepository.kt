package ru.nifontbus.materialdesign.ui.recycler.notes.room

import androidx.lifecycle.LiveData

interface LocalRepository {
    fun getAllNotes(): List<NoteEntity>
    fun saveNote(noteEntity: NoteEntity): Long
    fun deleteById(id: Long)
}