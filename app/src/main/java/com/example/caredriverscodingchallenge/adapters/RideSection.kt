package com.example.caredriverscodingchallenge.adapters

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.ParseJsonStuff
import com.example.caredriverscodingchallenge.R
import com.example.caredriverscodingchallenge.viewholders.HeaderViewHolder
import com.example.caredriverscodingchallenge.viewholders.RideViewHolder

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import java.text.NumberFormat
import java.util.*

private const val TAG = "RidesSection"

/** RideSection takes in a date for the header and a list of rides that correspond to that date.
 * @see: https://github.com/luizgrp/SectionedRecyclerViewAdapter */
class RideSection(
    context: Context, // Need context to access application resources (plurals)
    private val date: String,
    private val rides: List<Ride>,
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.list_item_ride)
        .headerResourceId(R.layout.list_item_trip_header)
        .build()
) {
    private val globFunc = ParseJsonStuff(context)
    private val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    override fun getContentItemsTotal(): Int = rides.size // number of items of this section

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        // return a custom instance of ViewHolder for the items of this section
        return RideViewHolder(view as ConstraintLayout)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: RideViewHolder = holder as RideViewHolder
        val startTime = rides[position].startsAt
        val endTime = rides[position].endsAt
        val orderedWaypoint = rides[position].orderedWaypoints
        val estEarnings = numberFormat.format(
            (rides[position].estimatedEarningsCents).div(100.0) // Div to get the cents
        )

        viewHolder.timeStart.text = globFunc.getTimeString(startTime)
        viewHolder.timeEnd.text = globFunc.getTimeString(endTime)
        viewHolder.numRiders.text = globFunc.getNumPassengersString(orderedWaypoint)
        viewHolder.orderedWaypoints.text = globFunc.getOrderedWaypointsString(orderedWaypoint)
        viewHolder.estEarnings.text = estEarnings

        viewHolder.rootView.setOnClickListener {
            clickListener.onItemRootViewClicked(rides[position].tripId)
        }
    }

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        // return an empty instance of ViewHolder for the headers of this section
        return HeaderViewHolder(view as ConstraintLayout)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val viewHolder: HeaderViewHolder = holder as HeaderViewHolder

        viewHolder.headerDate.text = date
        viewHolder.headerTimeRange.text = globFunc.getHeaderDateRange(rides)
        viewHolder.headerEstPrice.text = globFunc.getEstimatedEarnings(rides)
    }

    interface ClickListener {
        fun onItemRootViewClicked(
            tripId: Int
        )
    }
}

