package ru.nifontbus.materialdesign.ui.picture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nifontbus.materialdesign.BuildConfig
import ru.nifontbus.materialdesign.data.PODRetrofitImpl
import ru.nifontbus.materialdesign.data.PODServerResponseData
import ru.nifontbus.materialdesign.data.PictureOfTheDayData
import ru.nifontbus.materialdesign.data.StateFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PictureOfTheDayViewModel(
    val liveData: MutableLiveData<StateFragment> = MutableLiveData(StateFragment())
) : ViewModel() {

    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()

/*    init {
        liveData.value = StateFragment()
    }*/

    fun changeData(newDate: LocalDate) {
        liveData.value?.date = newDate
        sendServerRequest()
    }

    fun sendServerRequest() {
        liveData.value?.photo = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            // https://ru.minecraftfullmod.com/2026-working-with-dates-in-kotlin
            val currentDate = liveData.value!!.date
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val ds: String = currentDate.format(formatter)
//            Log.e("my", ds)

            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, ds).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveData.value?.photo =
                            PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveData.value?.photo =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveData.value?.photo =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                    liveData.notifyObserver()
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveData.value?.photo = PictureOfTheDayData.Error(t)
                    liveData.notifyObserver()
                }
            })
        }

    }
}

// https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata/49022687#49022687
fun <T> MutableLiveData<T>.notifyObserver() {
    value?.let { postValue(value) }
}
