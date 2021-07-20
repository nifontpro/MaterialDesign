package ru.nifontbus.materialdesign.app

import android.app.Application
import androidx.room.Room
import ru.nifontbus.materialdesign.ui.recycler.notes.room.MyDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private lateinit var appInstance: App
        private const val DB_NAME = "notes.db"

        private val history_dao by lazy {
            Room.databaseBuilder(
                appInstance.applicationContext,
                MyDatabase::class.java,
                DB_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .itemDao()
        }

        fun getHistoryDao() = history_dao
    }
}
