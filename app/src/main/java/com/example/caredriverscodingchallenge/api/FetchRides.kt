package com.example.caredriverscodingchallenge.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caredriverscodingchallenge.api.HsdApi
import com.example.caredriverscodingchallenge.api.HsdResponse
import com.example.caredriverscodingchallenge.api.RideResponse
import com.example.caredriverscodingchallenge.adapters.Ride
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson

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

    /* Tried to make an api call with @GET and @Query*/
    fun fetchRideWithId(tripId: Int): LiveData<Ride> {
        val responseLiveData: MutableLiveData<Ride> = MutableLiveData()
        val hsdRequest: Call<RideResponse> = hsdApi.getRideWithId(tripId)

        hsdRequest.enqueue(object : Callback<RideResponse> {
            override fun onFailure(call: Call<RideResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch ride with id", t)
            }

            override fun onResponse(call: Call<RideResponse>, response: Response<RideResponse>) {
                val body = Gson().toJson(response.body())
                Log.d(TAG, "Response received $body")

                val rideResponse: RideResponse? = response.body()
                val ride: Ride? = rideResponse?.ride
                responseLiveData.value = ride
            }
        })
        return responseLiveData
    }
}