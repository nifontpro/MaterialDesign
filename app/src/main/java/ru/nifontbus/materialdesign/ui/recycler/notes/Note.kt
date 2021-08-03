package ru.nifontbus.materialdesign.ui.recycler.notes

import java.time.LocalDate

const val TYPE_NOTE = 1

data class Note(
    var title: String = "",
    var description: String = "",
    var date: LocalDate = LocalDate.now(),
    val type: Int = TYPE_NOTE,
    var id: Long = 0,
    var deployed: Boolean = false
) {
    override fun toString(): String {
        return this.id.toString()
    }
}