package com.example.caredriverscodingchallenge.myrides

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.caredriverscodingchallenge.RideFetcher

class RideViewModel : ViewModel() {

    val rideItemLiveData: LiveData<List<Ride>> = RideFetcher().fetchRides()

}