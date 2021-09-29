package com.example.caredriverscodingchallenge.api

import retrofit2.Call
import retrofit2.http.GET

interface HsdApi {

    @GET("hsd-interview-resources/simplified_my_rides_response.json")

    fun fetchRides(): Call<HsdResponse>
}