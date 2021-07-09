package ru.nifontbus.materialdesign.data

import java.time.LocalDate

data class StateFragment(
    val photo: PictureOfTheDayData,
    var date: LocalDate = LocalDate.now()
)