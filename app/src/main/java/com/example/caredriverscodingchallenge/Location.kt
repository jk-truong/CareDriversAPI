package com.example.caredriverscodingchallenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Location {
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("lat")
    @Expose
    var lat: Double? = null

    @SerializedName("lng")
    @Expose
    var lng: Double? = null
}
