package ru.nifontbus.materialdesign.ui.recycler.notes.room

class LocalRepositoryImpl(private val localDataSource: NoteDao) : LocalRepository {

    override fun getAllNotes(): List<NoteEntity> {
        return localDataSource.getAll()
    }

    override fun saveNote(noteEntity: NoteEntity): Long {
        return localDataSource.insert(noteEntity)
    }

    override fun deleteById(id: Long) {
        localDataSource.deleteById(id)
    }

//    var liveList: LiveData<List<Person>> = MutableLiveData(repo.getAll().map { user ->
//        Person(user.firstName, user.age)
//    })
}