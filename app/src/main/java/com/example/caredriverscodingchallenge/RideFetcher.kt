package com.example.caredriverscodingchallenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caredriverscodingchallenge.api.HsdApi
import com.example.caredriverscodingchallenge.api.HsdResponse
import com.example.caredriverscodingchallenge.api.RideResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "RideFetcher"

class RideFetcher {

    private val hsdApi: HsdApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://storage.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        hsdApi = retrofit.create(HsdApi::class.java)
    }

    fun fetchRides(): LiveData<List<RideItem>> {
        val responseLiveData: MutableLiveData<List<RideItem>> = MutableLiveData()
        val hsdRequest: Call<HsdResponse> = hsdApi.fetchRides()

        hsdRequest.enqueue(object : Callback<HsdResponse> {

            override fun onFailure(call: Call<HsdResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch rides", t)
            }

            override fun onResponse(call: Call<HsdResponse>, response: Response<HsdResponse>) {
                Log.d(TAG, "Response received")
                val hsdResponse: HsdResponse? = response.body()
                val rideResponse: RideResponse? = hsdResponse?.rides
                var rideItems: List<RideItem> = rideResponse?.rideItems?: mutableListOf()
                responseLiveData.value = rideItems
            }
        })

        return responseLiveData
    }
}