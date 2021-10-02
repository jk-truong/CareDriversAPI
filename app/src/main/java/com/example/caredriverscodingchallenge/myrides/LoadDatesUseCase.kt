package com.example.caredriverscodingchallenge.myrides

import android.content.Context
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "LoadDatesUseCase"

class LoadDatesUseCase(private val ride: List<Ride>) {
    fun execute(context: Context): Map<String, List<Ride>> {

        val map: MutableMap<String, List<Ride>> = LinkedHashMap<String, List<Ride>>()

        /* Get the date range of all rides */
        map["Yo it's friday"] = ride
        /*var letter = 'A'
        while (letter <= 'Z') {
            val filteredRides: List<Ride> = getRidesWithDateRange(
                context.resources.getStringArray(R.array.names),
                letter
            )
            if (filteredRides.size > 0) {

            }
            letter++
        }*/
        Log.d(TAG, "LoadDatesUseCase hi $map")
        return map
    }

    private fun getRidesWithDateRange(rides: List<Ride>, date: Date): List<Ride> {
        val ridesList: MutableList<Ride> = ArrayList()

        for (ride in rides) {
            /*if (ride[0] == date) {
                ridesList.add(Ride(ride, getRandomImage(ride)))
            }*/
        }
        return ridesList
    }
}