package com.example.caredriverscodingchallenge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.Constants
import com.example.caredriverscodingchallenge.ParseJsonStuff
import com.example.caredriverscodingchallenge.R
import com.example.caredriverscodingchallenge.RideViewModel
import com.example.caredriverscodingchallenge.adapters.OrderedWaypoint
import com.example.caredriverscodingchallenge.adapters.Ride
import com.example.caredriverscodingchallenge.viewholders.AddressViewHolder
import java.text.NumberFormat
import java.util.*

private const val TAG = "RideDetailsFragment"
private const val ARG_TRIP_ID = "trip_id"

class RideDetailsFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var ride: Ride
    private lateinit var addressListRecyclerView: RecyclerView
    private lateinit var date: TextView
    private lateinit var timeRange: TextView
    private lateinit var estEarnings: TextView
    private lateinit var isSeries: TextView
    private lateinit var tripInfo: TextView
    private lateinit var btnCancelTrip: Button
    private lateinit var globFunc: ParseJsonStuff
    private lateinit var rideViewModel: RideViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        globFunc = ParseJsonStuff(requireContext())
        // SharedViewModel from activity. Use 'requireActivity' instead of 'this'
        rideViewModel = ViewModelProviders.of(requireActivity()).get(RideViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ride_details, container, false)

        toolbar = view.findViewById(R.id.toolbar)
        date = view.findViewById(R.id.text_date)
        timeRange = view.findViewById(R.id.text_time_range)
        estEarnings = view.findViewById(R.id.text_estimated_earnings)
        isSeries = view.findViewById(R.id.text_repeating_series)
        tripInfo = view.findViewById(R.id.text_trip_info)
        btnCancelTrip = view.findViewById(R.id.btn_cancel_trip)
        addressListRecyclerView = view.findViewById(R.id.recycler_view_address_list)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        addressListRecyclerView.layoutManager = layoutManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tripId = arguments?.getSerializable(ARG_TRIP_ID) as Int

        /* Set the back button only for this detail fragment */
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        /* Need to get the correct ride before we update any of the UI elements including
        * the recyclerview */
        rideViewModel.rideItemLiveData.observe(viewLifecycleOwner, { rideItem ->
            for (item in rideItem) { // Iterate until tripId matches, assuming that it is unique
                if (item.tripId == tripId) {
                    this.ride = item // Set ride
                    updateUI()
                    break
                }
            }
        })

        btnCancelTrip.setOnClickListener {
            Toast.makeText(context, "Cancel trip pressed", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI() {
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val timeRangeStr = globFunc.getFormattedDate(ride.startsAt, Constants.DATE_PARSE_PATTERN,
            Constants.DATE_TIME_PATTERN) + " — " + globFunc.getFormattedDate(ride.endsAt,
            Constants.DATE_PARSE_PATTERN, Constants.DATE_TIME_PATTERN)
        val tripInfoStr = "Trip ID: ${ride.tripId} • " +
                "${ride.estimatedRideMiles}mi • ${ride.estimatedRideMinutes} min"
        val orderedWaypoints = ride.orderedWaypoints

        date.text = globFunc.getFormattedDate(ride.startsAt,
            Constants.DATE_PARSE_PATTERN, Constants.DATE_HEADER_PATTERN)
        timeRange.text = timeRangeStr
        estEarnings.text = numberFormat.format(ride.estimatedEarningsCents.div(100.0))
        isSeries.isVisible = ride.inSeries
        tripInfo.text = tripInfoStr
        addressListRecyclerView.adapter = AddressAdapter(orderedWaypoints)
    }

    private inner class AddressAdapter(private val addressList: List<OrderedWaypoint>) :
        RecyclerView.Adapter<AddressViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AddressViewHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_address,
                parent,
                false
            ) as ConstraintLayout
            return AddressViewHolder(view)
        }

        override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
            val addressItem = addressList[position]
            val addressLine = addressItem.location.address
            var icon = ContextCompat.getDrawable(requireContext(), R.drawable.diamond)
            var anchorText = resources.getString(R.string.drop_off)
            if (addressItem.anchor) {
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.circle)
                anchorText = resources.getString(R.string.pickup)
            }

            holder.anchorIcon.background = icon
            holder.anchorText.text = anchorText
            holder.addressLine.text = addressLine
        }

        override fun getItemCount(): Int = addressList.size
    }

    companion object {
        fun newInstance(tripId: Int): RideDetailsFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TRIP_ID, tripId)
            }
            return RideDetailsFragment().apply {
                arguments = args
            }
        }
    }
}