package com.example.caredriverscodingchallenge

import android.content.Context
import android.util.Log
import com.example.caredriverscodingchallenge.adapters.OrderedWaypoint
import com.example.caredriverscodingchallenge.adapters.Ride
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ParseJsonStuff"

private val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

class ParseJsonStuff(context: Context) {
    var mcontext = context

    init {
        mcontext = context
    }

    /**@param rides Takes in a list of rides
     * @return the sum of all estimated earnings for that list of rides */
    fun getEstimatedEarnings(rides: List<Ride>): String {
        var total= 0.0
        for (ride in rides) {
            total += (ride.estimatedEarningsCents).div(100.0)
        }
        return numberFormat.format(total)
    }

    /**@param rides Takes in a list of rides
     * @return date range of all the rides */
    fun getHeaderDateRange(rides: List<Ride>): String {
        val beginDate = rides[0].startsAt
        val endDate = rides[rides.size - 1].endsAt
        return "${getTimeString(beginDate)} - ${getTimeString(endDate)}"
    }

    /** @param dateString Takes in a string date, parses and formats it
     * @return a formatted time, according to specified pattern, as a string */
    fun getTimeString(dateString: String): String {
        val dateParse = SimpleDateFormat(Constants.DATE_PARSE_PATTERN, Locale.US)
        val dateFormat = SimpleDateFormat(Constants.DATE_TIME_PATTERN, Locale.US)
        var formattedTime = ""

        try {
            val parsedTimeDate = dateParse.parse(dateString)!!
            formattedTime = dateFormat.format(parsedTimeDate)
        } catch (e: Exception) {
            Log.e(TAG, "Could not format the date $e")
        }
        return formattedTime
    }

    /**@param orderedWaypoint Takes in a list of OrderedWaypoint
     * @return a string containing number of riders and number of booster seats needed */
    fun getNumPassengersString(orderedWaypoint: List<OrderedWaypoint>): String {
        var numPassengersString = ""
        // TODO: Check for "anchor = true" instead of going to the first waypoint
        /* Find the number of passengers and set the appropriate plurals for 'rider' */
        val numPassengers = orderedWaypoint[0].passengers.size
        val stringRiders =
            mcontext.resources?.getQuantityString(R.plurals.riders, numPassengers)
        numPassengersString = "($numPassengers $stringRiders"

        /* Find the number of booster seats needed. Only need to look in the first waypoint
        * that is anchored because that contains all of the passengers throughout the ride.
        * (I could be wrong about this, maybe there are multiple starting waypoints but from
        * the given json I have inferred these rules) */
        var numBoosters = 0
        for (passenger in orderedWaypoint[0].passengers) {
            if (passenger.boosterSeat) {
                numBoosters++ // Increment counter for every booster seat
            }
        }

        /* If there are booster seats, concatenate number of booster seats required to the
        * numPassengersString. Else, just add closing parenthesis */
        numPassengersString += if (numBoosters > 0) {
            val stringBoosters =
                mcontext.resources?.getQuantityString(R.plurals.boosters, numBoosters)
            " • $numBoosters $stringBoosters)"
        } else {
            ")"
        }
        return numPassengersString
    }

    /**@param orderedWaypoint Takes in a list of OrderedWaypoint
     * @return a string containing the addresses with a line break between each address */
    fun getOrderedWaypointsString(orderedWaypoint: List<OrderedWaypoint>): String {
        /* For every waypoint, access location and get the address. Append the address to a
        * string */
        var addressString = ""
        var first = true

        for (i in orderedWaypoint.indices) {
            if (first) {
                first = false
            } else {
                addressString += "\n" // Prepend a line break for all lines except the first
            }
            addressString += "${i + 1}. ${orderedWaypoint[i].location.address}"
        }
        return addressString
    }

    /** Takes in a date string and returns a string with the date formatted according to the pattern
     * declared at the top of class.
     * @param dateString string containing your date
     * @param dateParsePattern the pattern of the date you are passing to dateString
     * @param dateFormatPattern determines how the return string looks
     * @return String */
    fun getFormattedDate(dateString: String, dateParsePattern: String,
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