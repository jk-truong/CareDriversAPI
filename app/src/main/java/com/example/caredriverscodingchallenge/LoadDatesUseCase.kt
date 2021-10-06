package com.example.caredriverscodingchallenge

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "LoadDatesUseCase"

class LoadDatesUseCase(private val rides: List<Ride>) {
    private val globFunc = ParseJsonStuff()

    /** @return a map containing days and list of rides in that day */
    fun execute(): Map<String, List<Ride>> {
        val map: MutableMap<String, List<Ride>> = LinkedHashMap<String, List<Ride>>()
        var currentDay = ""

        for (ride in rides) {
            // For every ride, get the day
            val day = globFunc.getFormattedDate(
                ride.startsAt,
                Constants.DATE_PARSE_PATTERN,
                Constants.DATE_YEAR_PATTERN
            )

            if (day != currentDay) {
                // retrieve list of rides in that day
                val filteredRides: List<Ride> = getRidesInCurrentDay(rides, day)

                // Create a map entry for every day (Key) with a list (value) of rides in that day
                if (filteredRides.isNotEmpty()) {
                    val headerDate = globFunc.getFormattedDate(
                        day,
                        Constants.DATE_YEAR_PATTERN,
                        Constants.DATE_HEADER_PATTERN
                    )
                    map[headerDate] = filteredRides
                }
                currentDay = day
            }
        }
        return map
    }

    private fun getRidesInCurrentDay(rides: List<Ride>, date: String): List<Ride> {
        val ridesList: MutableList<Ride> = ArrayList()

        for (ride in rides) {
            val currentRideDate = globFunc.getFormattedDate(
                ride.startsAt,
                Constants.DATE_PARSE_PATTERN,
                Constants.DATE_YEAR_PATTERN)
            if (currentRideDate == date) {
                ridesList.add(ride)
            }
        }
        return ridesList
    }
}