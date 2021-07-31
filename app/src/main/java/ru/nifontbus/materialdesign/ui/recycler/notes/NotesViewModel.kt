package ru.nifontbus.materialdesign.ui.recycler.notes

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nifontbus.materialdesign.app.App
import ru.nifontbus.materialdesign.ui.recycler.TYPE_HEADER
import ru.nifontbus.materialdesign.ui.recycler.notes.room.LocalRepository
import ru.nifontbus.materialdesign.ui.recycler.notes.room.LocalRepositoryImpl
import ru.nifontbus.materialdesign.ui.recycler.notes.room.NoteEntity

class NotesViewModel : ViewModel() {

    var liveData: MutableLiveData<MutableList<Note>> = MutableLiveData<MutableList<Note>>()

    private val localRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())

    private val handlerThread by lazy { HandlerThread("myThread") }
    private val handler by lazy { Handler(handlerThread.looper) }

    init {
        handlerThread.start()
        getAllItems()
    }

    private fun getAllItems() {
        handler.post {
            val notes = convertNotesEntityToNotesList(localRepository.getAllNotes()).toMutableList()
            notes.add(0, Note("Header", type = TYPE_HEADER))
            liveData.postValue(notes)
        }
    }

    fun insert(note: Note) {
        handler.post {
            note.id = 0 // Чтобы не было конфликта с существующей записью!!!
            val id = localRepository.saveNote(convertNoteToEntity(note))
            Log.e("my", "New Note Id = $id")
            if (id > -1) {
                note.id = id
                liveData.value?.add(note)
                liveData.notifyObserver()
            }
        }
    }

    fun deleteByPosition(position: Int) {
        Log.e("my", "Delete by pos $position livedata.size = ${liveData.value!!.size}")
        if (position > 0) {
            handler.post {
                localRepository.deleteById(liveData.value!![position].id)
                liveData.value!!.removeAt(position)
                liveData.notifyObserver()
            }
        }

    }
}

private fun convertNotesEntityToNotesList(entityList: List<NoteEntity>): List<Note> {
    return entityList.map {
        Note(
            it.title,
            it.description,
            it.date,
            it.type,
            it.id
        )
    }
}

private fun convertNoteToEntity(note: Note): NoteEntity {
    return NoteEntity(
        note.id,
        note.title,
        note.description,
        note.date,
        note.type
    )
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(value)
}