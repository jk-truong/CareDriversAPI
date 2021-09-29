package com.example.caredriverscodingchallenge

import android.location.Location
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderedWaypoint {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("anchor")
    @Expose
    var anchor: Boolean? = null

    @SerializedName("passengers")
    @Expose
    var passengers: List<Passenger>? = null

    @SerializedName("location")
    @Expose
    var location: Location? = null
}