package com.example.caredriverscodingchallenge.myrides

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.MainActivity
import com.example.caredriverscodingchallenge.R
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MyRidesFragment"
private const val DATE_PARSE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val DATE_FORMATTED_PATTERN = "h:mm a"
private const val ACTION_BAR_TITLE = "My Rides"

class MyRidesFragment : Fragment(), RidesSection.ClickListener {

    private lateinit var rideViewModel: RideViewModel
    private lateinit var ridesRecyclerView: RecyclerView

    private val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    private val dateParse: SimpleDateFormat = SimpleDateFormat(DATE_PARSE_PATTERN, Locale.US)
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        rideViewModel = ViewModelProviders.of(this).get(RideViewModel::class.java)

        (activity as MainActivity?)?.setActionBarTitle(ACTION_BAR_TITLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_rides, container, false)



        ridesRecyclerView = view.findViewById(R.id.recycler_view_my_rides)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        ridesRecyclerView.layoutManager = layoutManager


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionedAdapter = SectionedRecyclerViewAdapter()
        var ridesMap: Map<String, List<Ride>>

        rideViewModel.rideItemLiveData.observe(viewLifecycleOwner, { rideItems ->
            //Log.d(TAG, "Got ride items: $rideItems")
            ridesMap = LoadDatesUseCase(rideItems).execute(requireContext())
            for ((key, value) in ridesMap.entries) {
                if (value.isNotEmpty()) {
                    sectionedAdapter.addSection(context?.let { RidesSection(it, key, value, this) })
                }
            }
            ridesRecyclerView.adapter = sectionedAdapter
        })

    }

    override fun onItemRootViewClicked(section: RidesSection, itemAdapterPosition: Int) {
        Toast.makeText(context, "Position: $itemAdapterPosition", Toast.LENGTH_SHORT).show()
    }

    private inner class RideAdapter(private val ride: List<Ride>) :
        RecyclerView.Adapter<RideAdapter.RideHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            i: Int
        ): RideHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_ride,
                parent,
                false
            ) as ConstraintLayout
            return RideHolder(view)
        }

        override fun getItemCount(): Int = ride.size

        override fun onBindViewHolder(viewHolder: RideHolder, position: Int) {
            val parsedStartTime = dateParse.parse(ride[position].startsAt)!!
            val parsedEndTime = dateParse.parse(ride[position].endsAt)!!
            val orderedWaypoint = ride[position].orderedWaypoints
            val estPrice = numberFormat.format(
                (ride[position].estimatedEarningsCents).div(100.0) // Div to get the cents
            )

            viewHolder.timeStart.text = getFormattedTimeString(parsedStartTime)
            viewHolder.timeEnd.text = getFormattedTimeString(parsedEndTime)
            viewHolder.numRiders.text = getNumPassengersString(orderedWaypoint)
            viewHolder.orderedWaypoints.text = getOrderedWaypointsString(orderedWaypoint)
            viewHolder.estPrice.text = estPrice

            viewHolder.bindRideItem(ride[position])
        }

        /** Takes in a parsed date, formats it according to the pattern and returns a string with
         * the time. */
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
         * and boosters. */
        private fun getNumPassengersString(orderedWaypoint: List<OrderedWaypoint>): String {
            var numPassengersString = ""

            /* Find the number of passengers and set the appropriate plurals for 'rider' */
            val numPassengers = orderedWaypoint[0].passengers.size
            val stringRiders =
                context?.resources?.getQuantityString(R.plurals.riders, numPassengers)
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
                    context?.resources?.getQuantityString(R.plurals.boosters, numBoosters)
                " • $numBoosters $stringBoosters)"
            } else {
                ")"
            }
            return numPassengersString
        }

        /** Takes in a list of OrderedWaypoint and returns a string containing the addresses.
         * Each address has a line break. */
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

        fun addHeaderAndSubmitList(list: List<OrderedWaypoint>?) {
            adapterScope.launch {
                /*val items = when (true) {
                    null -> listOf(DataItem.Header)
                    else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
                }
                withContext(Dispatchers.Main) {

                }*/
            }
        }

        /** Holder for the ride card */
        inner class RideHolder(itemLayout: ConstraintLayout)
            : RecyclerView.ViewHolder(itemLayout),
            View.OnClickListener {
            val timeStart: TextView = itemView.findViewById(R.id.text_card_starts_at)
            val timeEnd: TextView = itemView.findViewById(R.id.text_card_ends_at)
            val estPrice: TextView = itemView.findViewById(R.id.text_card_estimated_price)
            val numRiders: TextView = itemView.findViewById(R.id.text_card_num_passengers)
            val orderedWaypoints: TextView = itemView.findViewById(R.id.text_card_ordered_waypoints)

            private lateinit var ride: Ride

            init {
                itemView.setOnClickListener(this)
            }

            fun bindRideItem(item: Ride) {
                ride = item
            }

            override fun onClick(view: View) {
                Toast.makeText(context, "Ride trip id: ${ride.tripId}", Toast.LENGTH_SHORT).show()
                // TODO: Create intent, open new fragment
            }
        }

        /** Holder for the header. Needs to be displayed for every day (section) that has rides. */
        inner class HeaderHolder(itemLayout: ConstraintLayout):
            RecyclerView.ViewHolder(itemLayout) {
            val headerDate: TextView = itemView.findViewById(R.id.text_header_date)
            val headerTimeRange: TextView = itemView.findViewById(R.id.text_header_time_range)
            val headerEstPrice: TextView = itemView.findViewById(R.id.text_card_estimated_price)
        }
    }

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }


}







