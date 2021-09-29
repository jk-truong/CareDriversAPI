package com.example.caredriverscodingchallenge

import com.google.gson.annotations.SerializedName

data class RideItem(
    var id: String = "",
    @SerializedName("url_s") var url: String = ""
)