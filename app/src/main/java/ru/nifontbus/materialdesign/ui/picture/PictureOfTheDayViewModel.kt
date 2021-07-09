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
import java.time.format.DateTimeFormatter

class PictureOfTheDayViewModel(
    val liveData: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) :
    ViewModel() {

    fun sendServerRequest() {
        liveData.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            // https://ru.minecraftfullmod.com/2026-working-with-dates-in-kotlin
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var ds: String =  currentDate.format(formatter)
            Log.e("my",ds)

            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, ds).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveData.value =
                            PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveData.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveData.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveData.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}

// https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata/49022687#49022687
fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(value)
}
