package ru.nifontbus.materialdesign.ui.recycler.notes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nifontbus.materialdesign.ui.recycler.notes.Note

@Dao
interface NoteDao : BaseDao<NoteEntity> {

    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

    @Query("DELETE FROM NoteEntity WHERE id = :id")
    fun deleteById(id: Long)
}