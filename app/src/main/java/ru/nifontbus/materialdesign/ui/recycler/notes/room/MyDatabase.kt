package ru.nifontbus.materialdesign.ui.recycler.notes.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase: RoomDatabase() {
    abstract fun itemDao() : NoteDao
}