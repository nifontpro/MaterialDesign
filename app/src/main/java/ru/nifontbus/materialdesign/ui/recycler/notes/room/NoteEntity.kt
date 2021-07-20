package ru.nifontbus.materialdesign.ui.recycler.notes.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.nifontbus.materialdesign.ui.recycler.notes.TYPE_NOTE
import java.time.LocalDate

@Entity
@TypeConverters(value = [Converters::class]) // Для преобразования LocalDate <--> Long
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String = "",
    val description : String = "",
    val date: LocalDate = LocalDate.now(),
    val type: Int = TYPE_NOTE
)