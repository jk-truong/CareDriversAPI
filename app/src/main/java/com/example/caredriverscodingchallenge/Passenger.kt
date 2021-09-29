package com.example.caredriverscodingchallenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Passenger {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("booster_seat")
    @Expose
    var boosterSeat: Boolean? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
}