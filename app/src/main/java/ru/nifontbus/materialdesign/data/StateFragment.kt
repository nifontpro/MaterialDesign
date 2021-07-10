package ru.nifontbus.materialdesign.data

import java.time.LocalDate

data class StateFragment (
    var photo: PictureOfTheDayData = PictureOfTheDayData.Loading(null),
    var date: LocalDate = LocalDate.now()
)