package ru.nifontbus.materialdesign.ui.view_pager

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

class PhotoViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
) : ViewModel() {

    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
    lateinit var photoDate: LocalDate

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
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateString: String =  photoDate.format(formatter)
            Log.e("my",dateString)

            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, dateString).enqueue(object :
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
