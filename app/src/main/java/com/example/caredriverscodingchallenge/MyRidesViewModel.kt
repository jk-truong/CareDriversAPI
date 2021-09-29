package com.example.caredriverscodingchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MyRidesViewModel : ViewModel() {

    val myRidesItemLiveData: LiveData<List<RideItem>>

    init {
        myRidesItemLiveData = RideFetcher().fetchRides()
    }
}