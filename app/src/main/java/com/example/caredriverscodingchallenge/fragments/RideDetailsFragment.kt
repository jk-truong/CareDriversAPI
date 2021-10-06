package com.example.caredriverscodingchallenge.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.Constants
import com.example.caredriverscodingchallenge.Constants.MAPVIEW_BUNDLE_KEY
import com.example.caredriverscodingchallenge.ParseJsonStuff
import com.example.caredriverscodingchallenge.R
import com.example.caredriverscodingchallenge.RideViewModel
import com.example.caredriverscodingchallenge.adapters.AddressAdapter
import com.example.caredriverscodingchallenge.Ride
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.NumberFormat
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.BitmapDescriptorFactory


private const val TAG = "RideDetailsFragment"
private const val ARG_TRIP_ID = "trip_id"
private const val LOCATION_REQUEST_CODE = 106

class RideDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var toolbar: Toolbar
    private lateinit var ride: Ride
    private lateinit var addressListRecyclerView: RecyclerView
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
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
        globFunc = ParseJsonStuff()
        // SharedViewModel from activity
        rideViewModel = ViewModelProvider(requireActivity()).get(RideViewModel::class.java)
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
        mapView = view.findViewById(R.id.map_view)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        addressListRecyclerView.layoutManager = layoutManager

        initGoogleMap(savedInstanceState)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Set the back button only for this fragment */
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        rideViewModel.getRide().observe(viewLifecycleOwner, { rideItem ->
            Log.d(TAG, "got ride: $rideItem")
            ride = rideItem
            updateUI()
        })

        btnCancelTrip.setOnClickListener {
            Toast.makeText(context, "Cancel trip pressed", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI() {
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val startTime = globFunc.getFormattedDate(ride.startsAt, Constants.DATE_PARSE_PATTERN,
            Constants.DATE_TIME_PATTERN)
        val endTime = globFunc.getFormattedDate(ride.endsAt, Constants.DATE_PARSE_PATTERN,
            Constants.DATE_TIME_PATTERN)
        val headerDate = globFunc.getFormattedDate(ride.startsAt,
            Constants.DATE_PARSE_PATTERN, Constants.DATE_HEADER_PATTERN)
        val timeRangeStr = "$startTime — $endTime"
        val tripInfoStr = "Trip ID: ${ride.tripId} • " +
                "${ride.estimatedRideMiles}mi • ${ride.estimatedRideMinutes} min"
        val estEarningsDollars = numberFormat.format(ride.estimatedEarningsCents.div(100.0))
        val orderedWaypoints = ride.orderedWaypoints

        date.text = headerDate
        timeRange.text = timeRangeStr
        estEarnings.text = estEarningsDollars
        isSeries.isVisible = ride.inSeries
        tripInfo.text = tripInfoStr
        addressListRecyclerView.adapter = AddressAdapter(
            requireContext(),
            layoutInflater,
            orderedWaypoints
        )
    }

    override fun onMapReady(retmap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permissions, LOCATION_REQUEST_CODE)
            return
        }
        map = retmap
        // Wait until map has been initialized before we can add markers
        mapView.getMapAsync { googleMap ->
            googleMap.isMyLocationEnabled = true
            setUpMap()
        }
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        /* *** IMPORTANT ***
        * MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        * objects or sub-Bundles. */
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
    }

    /** Zooms a Route (given a List of LalLng) at the greatest possible zoom level.
     * @param googleMap: instance of GoogleMap
     * @param lstLatLngRoute: list of LatLng forming Route
     */
    private fun zoomRoute(googleMap: GoogleMap?, lstLatLngRoute: List<LatLng?>?) {
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return
        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint!!)
        val routePadding = 200
        val latLngBounds = boundsBuilder.build()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding))
    }

    private fun setUpMap() {
        val orderedWaypoints = ride.orderedWaypoints
        val latLngList: MutableList<LatLng> = mutableListOf()

        map.uiSettings.isZoomControlsEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        // Set marker for map position
        for (address in orderedWaypoints) {
            val lat = address.location.lat
            val lng = address.location.lng
            // Green = start, Red = end waypoint
            var markerColor = BitmapDescriptorFactory.HUE_RED
            if (address.anchor) {
                markerColor = BitmapDescriptorFactory.HUE_GREEN
            }
            map.addMarker(MarkerOptions()
                .position(LatLng(lat, lng))
                .title("Marker ${address.id}")
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor)))
            latLngList.add(LatLng(lat,lng)) // Some of the coordinates overlap after looking at the JSON
            zoomRoute(map, latLngList)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Location permission granted")
                initGoogleMap(null)
            } else {
                Toast.makeText(
                    requireContext(), "Location permission denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        fun newInstance(): RideDetailsFragment {
            return RideDetailsFragment()
        }
    }
}