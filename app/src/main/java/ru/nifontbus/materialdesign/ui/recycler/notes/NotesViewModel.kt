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

class NotesViewModel :  ViewModel() {

    var liveData: MutableLiveData<MutableList<Note>> = MutableLiveData()

    private val localRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())

    private val handlerThread by lazy { HandlerThread("myThread") }
    private val handler by lazy { Handler(handlerThread.looper) }

    init {
        handlerThread.start()
        getAllItems()
    }

    private fun getAllItems() {
        handler.post {
            val notes = localRepository.getAllNotes().toMutableList()
            notes.add(0, Note("Header", type = TYPE_HEADER))
            liveData.postValue(notes)
        }
    }

    fun insert(note: Note) {
        handler.post {
            val id = localRepository.saveNote(note)
//            note.id = id
            liveData.value?.add(note)
            liveData.notifyObserver()
        }
    }

    fun deleteByPosition(position: Int) {
        Log.e("my", "Delete by pos $position livedata.size = ${liveData.value!!.size}")
        if (position > 0 ) {
            handler.post {
                localRepository.deleteById(liveData.value!![position].id)
                liveData.value!!.removeAt(position)
                liveData.notifyObserver()
            }
        }

    }
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(value)
}