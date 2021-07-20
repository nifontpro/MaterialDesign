package ru.nifontbus.materialdesign.ui.recycler.notes.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PersonDao : BaseDao<NoteEntity> {

    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

}