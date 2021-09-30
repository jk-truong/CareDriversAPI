package com.example.caredriverscodingchallenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    private inner class RideAdapter(private val ride: List<Ride>) :
        RecyclerView.Adapter<RideAdapter.ViewHolder?>() {

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
        ): ViewHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_ride,
                viewGroup,
                false
            )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = ride.size

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            /* Set start and end time */
            try {
                val formattedStartTime = dateFormat.format(dateParse.parse(ride[position].startsAt))
                val formattedEndTime = dateFormat.format(dateParse.parse(ride[position].endsAt))

                viewHolder.timeStart.text = formattedStartTime
                viewHolder.timeEnd.text = formattedEndTime
            } catch (e: Exception) {
                Log.e(TAG, "Could not parse and format dates $e")
            }

            /* Set estimated price, number of riders, and number of booster seats */
            val numPassengers = ride[position].orderedWaypoints?.get(0)?.passengers!!.size
            var numPassengersString = "$numPassengers" + " " +
                    context?.resources?.getQuantityString(R.plurals.riders, numPassengers)

            val orderedWaypoint = ride[position].orderedWaypoints!!
            var numBoosters = 0
            for (i in 0 until orderedWaypoint[0].passengers?.size!!) {
                if (orderedWaypoint[0].passengers!![i].boosterSeat == true) {
                    numBoosters ++ // Increment counter for every booster
                }
            }
            if (numBoosters > 0) {
                numPassengersString += " - $numBoosters " +
                        context?.resources?.getQuantityString(R.plurals.boosters, numBoosters)
            }

            viewHolder.estPrice.text =
                numberFormat.format((ride[position].estimatedEarningsCents)?.div(100.0))
            viewHolder.numRiders.text = numPassengersString
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val timeStart: TextView = itemView.findViewById(R.id.text_card_starts_at)
            val timeEnd: TextView = itemView.findViewById(R.id.text_card_ends_at)
            val estPrice: TextView = itemView.findViewById(R.id.text_card_estimated_price)
            val numRiders: TextView = itemView.findViewById(R.id.text_card_num_passengers)
            val orderedWaypoints: RecyclerView = itemView.findViewById(R.id.recycler_view_card_ordered_waypoints)
        }
    }

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







