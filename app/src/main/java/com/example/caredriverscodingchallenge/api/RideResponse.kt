package com.example.caredriverscodingchallenge.api

import com.example.caredriverscodingchallenge.RideItem
import com.google.gson.annotations.SerializedName

class RideResponse {
    @SerializedName("ride")
    lateinit var rideItems: List<RideItem>
}