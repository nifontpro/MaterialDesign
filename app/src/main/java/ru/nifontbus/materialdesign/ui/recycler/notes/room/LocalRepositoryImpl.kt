package ru.nifontbus.materialdesign.ui.recycler.notes.room

import ru.nifontbus.materialdesign.ui.recycler.notes.Note

class LocalRepositoryImpl(private val localDataSource: NoteDao) : LocalRepository {

    override fun getAllNotes(): List<Note> {
        return convertNotesEntityToPersonList(localDataSource.getAll())
    }

//    override fun saveNote(note: Note): Long {
    override fun saveNote(note: Note) {
//        return localDataSource.insert(convertPersonToEntity(note))
        localDataSource.insert(convertPersonToEntity(note))
    }

    override fun deleteById(id: Long) {
        localDataSource.deleteById(id)
    }

    private fun convertNotesEntityToPersonList(entityList: List<NoteEntity>): List<Note> {
        return entityList.map {
            Note(
                it.title,
                it.description,
//                Converters.fromTimestamp(it.date),
                it.date,
                it.type,
                it.id
            )
        }
    }

    private fun convertPersonToEntity(note: Note): NoteEntity {
        return NoteEntity(
            note.id,
            note.title,
            note.description,
//            Converters.dateToTimestamp(note.date),
            note.date,
            note.type
        )
    }

//    var liveList: LiveData<List<Person>> = MutableLiveData(repo.getAll().map { user ->
//        Person(user.firstName, user.age)
//    })
}