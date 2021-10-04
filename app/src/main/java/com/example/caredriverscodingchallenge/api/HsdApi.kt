package com.example.caredriverscodingchallenge.api

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HsdApi {

    @GET("hsd-interview-resources/simplified_my_rides_response.json")
    fun fetchRides(): Call<HsdResponse>
}