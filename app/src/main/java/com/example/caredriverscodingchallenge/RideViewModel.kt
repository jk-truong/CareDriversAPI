package com.example.caredriverscodingchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.caredriverscodingchallenge.adapters.Ride
import com.example.caredriverscodingchallenge.api.RideFetcher

/** Shared ViewModel */
class RideViewModel : ViewModel() {
    // Read only
    val rideItemLiveData: LiveData<List<Ride>> = RideFetcher().fetchRides()

    private val selectedRideItemLiveData = MutableLiveData<Ride>()

    fun setRide(item: Ride) {
        selectedRideItemLiveData.value = item
    }

    fun getRide(): MutableLiveData<Ride> {
        return selectedRideItemLiveData
    }

}