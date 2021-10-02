package com.example.caredriverscodingchallenge.myrides

import android.content.Context
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.R
import com.example.caredriverscodingchallenge.RideViewHolder

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "RidesSection"
private const val DATE_PARSE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val DATE_FORMATTED_PATTERN = "h:mm a"

/** RidesSection takes in a date for the header and a list of rides that correspond to that date.
 * @see: https://github.com/luizgrp/SectionedRecyclerViewAdapter */
class RidesSection(
    private val context: Context, // Need context to access application resources (plurals)
    private val date: String,
    private val ride: List<Ride>,
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.list_item_ride)
        .headerResourceId(R.layout.list_item_trip_header)
        .build()
) {

    private val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    private val dateParse: SimpleDateFormat = SimpleDateFormat(DATE_PARSE_PATTERN, Locale.US)

    override fun getContentItemsTotal(): Int = ride.size // number of items of this section

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        // return a custom instance of ViewHolder for the items of this section
        return RideViewHolder(view as ConstraintLayout)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: RideViewHolder = holder as RideViewHolder
        val parsedStartTime = dateParse.parse(ride[position].startsAt)!!
        val parsedEndTime = dateParse.parse(ride[position].endsAt)!!
        val orderedWaypoint = ride[position].orderedWaypoints
        val estPrice = numberFormat.format(
            (ride[position].estimatedEarningsCents).div(100.0) // Div to get the cents
        )
        // bind your view here
        viewHolder.timeStart.text = getFormattedTimeString(parsedStartTime)
        viewHolder.timeEnd.text = getFormattedTimeString(parsedEndTime)
        viewHolder.numRiders.text = getNumPassengersString(orderedWaypoint)
        viewHolder.orderedWaypoints.text = getOrderedWaypointsString(orderedWaypoint)
        viewHolder.estPrice.text = estPrice

        viewHolder.rootView.setOnClickListener {
            clickListener.onItemRootViewClicked(this, viewHolder.adapterPosition)
        }
    }

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        // return an empty instance of ViewHolder for the headers of this section
        return HeaderViewHolder(view as ConstraintLayout)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val viewHolder: HeaderViewHolder = holder as HeaderViewHolder

        viewHolder.headerDate.text = date
        viewHolder.headerTimeRange.text = "5:00pm - 5:15pm"
        viewHolder.headerEstPrice.text = "toomuch4u"
    }

    /** Takes in a parsed date, formats it according to the pattern and returns a string with
     * the time.
     * @return a formatted time as a string */
    private fun getFormattedTimeString(parsedTimeDate: Date): String {
        /* Set start and end time */
        var formattedTime = ""
        try {
            val dateFormat = SimpleDateFormat(DATE_FORMATTED_PATTERN, Locale.US)
            formattedTime = dateFormat.format(parsedTimeDate)
        } catch (e: Exception) {
            Log.e(TAG, "Could not format the date $e")
        }
        return formattedTime
    }

    /** Takes in a list of OrderedWaypoint and returns a string containing number of riders
     * and booster seats needed.
     * @return a string containing number of riders and number of booster seats */
    private fun getNumPassengersString(orderedWaypoint: List<OrderedWaypoint>): String {
        var numPassengersString = ""

        /* Find the number of passengers and set the appropriate plurals for 'rider' */
        val numPassengers = orderedWaypoint[0].passengers.size
        val stringRiders =
            context.resources?.getQuantityString(R.plurals.riders, numPassengers)
        numPassengersString = "($numPassengers $stringRiders"

        /* Find the number of booster seats needed. Only need to look in the first waypoint
        * that is anchored because that contains all of the passengers throughout the ride.
        * (I could be wrong about this, but from the given json I have inferred these rules) */
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
                context.resources?.getQuantityString(R.plurals.boosters, numBoosters)
            " • $numBoosters $stringBoosters)"
        } else {
            ")"
        }
        return numPassengersString
    }

    /** Takes in a list of OrderedWaypoint and returns a string containing the addresses.
     * Each address has a line break.
     * @return a string containing the addresses */
    private fun getOrderedWaypointsString(orderedWaypoint: List<OrderedWaypoint>): String {
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

    interface ClickListener {
        fun onItemRootViewClicked(
            section: RidesSection,
            itemAdapterPosition: Int
        )
    }
}

