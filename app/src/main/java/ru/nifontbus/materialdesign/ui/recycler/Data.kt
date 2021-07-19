package ru.nifontbus.materialdesign.ui.recycler

const val TYPE_EARTH = 0
const val TYPE_MARS = 1
const val TYPE_HEADER = 2
//const val TYPE_JUPITER = 2

data class Data(
    var someText: String = "",
    var someDescription: String? = "",
    var deployed: Boolean = false,
    val type: Int = TYPE_MARS
) {
    var id: Int = currentId

    init {
        currentId++
    }

    companion object {
        var currentId = 0
        fun initId() {
            currentId = 0
        }
    }
}