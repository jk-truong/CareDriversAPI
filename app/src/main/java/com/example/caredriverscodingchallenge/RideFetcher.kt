package com.example.caredriverscodingchallenge

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caredriverscodingchallenge.api.HsdApi
import com.example.caredriverscodingchallenge.api.HsdResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import kotlin.coroutines.coroutineContext

private const val TAG = "RideFetcher"

/** This is the retrofit configuration and API direct access */
class RideFetcher {

    private val hsdApi: HsdApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://storage.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        hsdApi = retrofit.create(HsdApi::class.java)
    }

    /** Returns a MutableLiveData list of rides */
    fun fetchRides(): LiveData<List<Ride>> {
        val responseLiveData: MutableLiveData<List<Ride>> = MutableLiveData()
        val hsdRequest: Call<HsdResponse> = hsdApi.fetchRides()

        hsdRequest.enqueue(object : Callback<HsdResponse> {

            override fun onFailure(call: Call<HsdResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch rides", t)
            }

            override fun onResponse(call: Call<HsdResponse>, response: Response<HsdResponse>) {
                val body = Gson().toJson(response.body())
                Log.d(TAG, "Response received $body")

                val hsdResponse: HsdResponse? = response.body()
                val rides: List<Ride> = hsdResponse?.rides?: mutableListOf() // List of rides from the response
                responseLiveData.value = rides // Set list of rides to live data list
            }
        })

        return responseLiveData
    }
}