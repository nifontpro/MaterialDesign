package ru.nifontbus.materialdesign.ui.recycler.notes.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface NoteDao : BaseDao<NoteEntity> {

    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

    @Query("DELETE FROM NoteEntity WHERE id = :id")
    fun deleteById(id: Long)
}