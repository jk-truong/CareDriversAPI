package com.example.caredriverscodingchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RideViewModel : ViewModel() {

    val rideItemLiveData: LiveData<List<Ride>>

    init {
        rideItemLiveData = RideFetcher().fetchRides()
    }

}