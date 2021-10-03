package com.example.caredriverscodingchallenge.myrides

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "LoadDatesUseCase"
private const val DATE_FROM_JSON_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val DATE_FORMATTED_PATTERN = "yyyy/DD"
private const val DATE_HEADER_FORMAT = "E M/dd"

class LoadDatesUseCase(private val rides: List<Ride>) {

    /** @return a mutable map containing days and list of rides in that day */
    fun execute(): Map<String, List<Ride>> {
        val map: MutableMap<String, List<Ride>> = LinkedHashMap<String, List<Ride>>()
        var currentDay = ""

        for (ride in rides) {
            // For every ride, get the day
            val day = getFormattedDate(
                ride.startsAt,
                DATE_FROM_JSON_PATTERN,
                DATE_FORMATTED_PATTERN
            )

            if (day != currentDay) {
                // retrieve list of rides in that day
                val filteredRides: List<Ride> = getRidesInCurrentDay(rides, day)

                // Create a map entry for every day (Key) with a list (value) of rides in that day
                if (filteredRides.isNotEmpty()) {
                    val headerDate = getFormattedDate(
                        day,
                        DATE_FORMATTED_PATTERN,
                        DATE_HEADER_FORMAT
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
            val currentRideDate = getFormattedDate(
                ride.startsAt,
                DATE_FROM_JSON_PATTERN,
                DATE_FORMATTED_PATTERN)
            if (currentRideDate == date) {
                ridesList.add(ride)
            }
        }
        return ridesList
    }

    /** Takes in a date string and returns a string with the date formatted according to the pattern
     * declared at the top of class.
     * @param dateString string containing your date
     * @param dateParsePattern define the pattern of the date you are passing to dateString
     * @param dateFormatPattern returns date in this format
     * @return String */
    private fun getFormattedDate(dateString: String, dateParsePattern: String,
                                 dateFormatPattern: String): String {
        val dateParse = SimpleDateFormat(dateParsePattern, Locale.US)
        val dateFormat = SimpleDateFormat(dateFormatPattern, Locale.US)
        var dateStr = ""

        try {
            val dateObj = dateParse.parse(dateString)!!
            dateStr = dateFormat.format(dateObj)
        } catch (e: Exception) {
            Log.e(TAG, "Could not parse dates", e)
        }
        return dateStr
    }
}