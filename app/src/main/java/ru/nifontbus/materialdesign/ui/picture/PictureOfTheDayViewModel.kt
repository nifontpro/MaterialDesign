package ru.nifontbus.materialdesign.ui.picture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nifontbus.materialdesign.BuildConfig
import ru.nifontbus.materialdesign.data.PODRetrofitImpl
import ru.nifontbus.materialdesign.data.PODServerResponseData
import ru.nifontbus.materialdesign.data.PictureOfTheDayData
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) :
    ViewModel() {

    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            // https://ru.minecraftfullmod.com/2026-working-with-dates-in-kotlin
            val currentDate = LocalDate.now()
            val date1 = currentDate.minusDays(1)
            val date2 = currentDate.minusDays(2)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var ds1: String =  currentDate.format(formatter)
            var ds2: String =  date1.format(formatter)
            var ds3: String =  date2.format(formatter)
            Log.e("my",ds1)
            Log.e("my",ds2)
            Log.e("my",ds3)

            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, ds1).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}
