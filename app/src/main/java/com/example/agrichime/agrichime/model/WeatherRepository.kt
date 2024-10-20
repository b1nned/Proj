package com.example.agrichime.agrichime.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrichime.agrichime.model.data.WeatherRootList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {

    val data = MutableLiveData<WeatherRootList>()

    fun getWeather(): LiveData<String> {
        val response: Call<WeatherRootList> =
            WeatherApi.weatherInstances.getWeather("16.413839", "120.590797")

        val weathRes = MutableLiveData<String>()

        response.enqueue(object : Callback<WeatherRootList> {
            override fun onFailure(call: Call<WeatherRootList>, t: Throwable) {
                Log.d("WeatherRepository", "Error Occured")
            }

            override fun onResponse(
                call: Call<WeatherRootList>,
                response: Response<WeatherRootList>
            ) {
                if (response.isSuccessful) {
                    data.value = response.body()
                    weathRes.value = "DONE"
                } else {
                    weathRes.value = "FAILED"
                }
            }
        })
        return weathRes
    }

}