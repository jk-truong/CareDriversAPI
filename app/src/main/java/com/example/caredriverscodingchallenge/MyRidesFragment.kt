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
            try {
                val formattedStartTime = dateFormat.format(dateParse.parse(ride[position].startsAt))
                val formattedEndTime = dateFormat.format(dateParse.parse(ride[position].endsAt))

                viewHolder.timeStart.text = formattedStartTime
                viewHolder.timeEnd.text = formattedEndTime
            } catch (e: Exception) {
                Log.e(TAG, "Could not parse and format dates $e")
            }


            viewHolder.estPrice.text = numberFormat.format((ride[position].estimatedEarningsCents)?.div(
                100.0
            ))
            viewHolder.numRiders.text = ride[position].orderedWaypoints?.size.toString()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val timeStart: TextView = itemView.findViewById(R.id.text_card_starts_at)
            val timeEnd: TextView = itemView.findViewById(R.id.text_card_ends_at)
            val estPrice: TextView = itemView.findViewById(R.id.text_card_estimated_price)
            val numRiders: TextView = itemView.findViewById(R.id.text_card_num_riders)
        }
    }

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







