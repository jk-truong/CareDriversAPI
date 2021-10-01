package com.example.caredriverscodingchallenge

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
import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MyRidesFragment"
private const val DATE_PARSE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val ACTION_BAR_TITLE = "My Rides"

class MyRidesFragment : Fragment() {

    private lateinit var rideViewModel: RideViewModel
    private lateinit var ridesRecyclerView: RecyclerView

    val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateParse: SimpleDateFormat = SimpleDateFormat(DATE_PARSE_PATTERN, Locale.US)
    val dateFormat: SimpleDateFormat = SimpleDateFormat("h:mm a", Locale.US)

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
        rideViewModel.rideItemLiveData.observe(viewLifecycleOwner, { rideItems ->
            // Log.d(TAG, "Got ride items: $rideItems")
            ridesRecyclerView.adapter = RideAdapter(rideItems)
        })

    }

    private inner class RideHolder(itemLayout: ConstraintLayout)
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
            Toast.makeText(context, "View clicked ${ride.tripId}", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class RideAdapter(private val ride: List<Ride>) :
        RecyclerView.Adapter<RideHolder>() {

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
        ): RideHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_ride,
                viewGroup,
                false
            ) as ConstraintLayout
            return RideHolder(view)
        }

        override fun getItemCount(): Int = ride.size

        override fun onBindViewHolder(viewHolder: RideHolder, position: Int) {
            /* Set start and end time */
            try {
                val parsedStartTime = dateParse.parse(ride[position].startsAt)!!
                val parsedEndTime = dateParse.parse(ride[position].endsAt)!!
                val formattedStartTime = dateFormat.format(parsedStartTime)
                val formattedEndTime = "— " + dateFormat.format(parsedEndTime)

                viewHolder.timeStart.text = formattedStartTime
                viewHolder.timeEnd.text = formattedEndTime
            } catch (e: Exception) {
                Log.e(TAG, "Could not parse and format dates $e")
            }

            /* Find the number of passengers and set the appropriate plurals for 'rider' */
            val numPassengers = ride[position].orderedWaypoints[0].passengers.size
            val stringRiders = context?.resources?.getQuantityString(R.plurals.riders, numPassengers)
            var numPassengersString = "($numPassengers $stringRiders"

            /* Find the number of booster seats needed. Only need to look in the first waypoint
            * that is anchored because that contains all of the passengers throughout the ride.
            * (I could be wrong about this, but from the given json I have inferred these rules) */
            val orderedWaypoint = ride[position].orderedWaypoints
            var numBoosters = 0
            for (passenger in orderedWaypoint[0].passengers) {
                if (passenger.boosterSeat) {
                    numBoosters ++ // Increment counter for every booster seat
                }
            }

            /* If there are booster seats, concatenate number of booster seats required to the
            * numPassengersString. Else, just add closing parenthesis */
            numPassengersString += if (numBoosters > 0) {
                val stringBoosters = context?.resources?.getQuantityString(R.plurals.boosters, numBoosters)
                " • $numBoosters $stringBoosters)"
            } else {
                ")"
            }

            /* For every waypoint, access location and get the address. Append the address to a
            * string */
            var addressString = ""
            for (i in orderedWaypoint.indices) {
                addressString += "${i+1}. ${orderedWaypoint[i].location.address}\n"
            }

            /* Set estimated price, number of riders, and number of booster seats */
            viewHolder.estPrice.text =
                numberFormat.format((ride[position].estimatedEarningsCents).div(100.0))
            viewHolder.numRiders.text = numPassengersString
            viewHolder.orderedWaypoints.text = addressString


            viewHolder.bindRideItem(ride[position])
        }
    }

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







