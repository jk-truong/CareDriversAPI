package com.example.caredriverscodingchallenge.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.MainActivity
import com.example.caredriverscodingchallenge.R
import com.example.caredriverscodingchallenge.RideViewModel
import com.example.caredriverscodingchallenge.LoadDatesUseCase
import com.example.caredriverscodingchallenge.Ride
import com.example.caredriverscodingchallenge.adapters.RideSection
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

private const val TAG = "MyRidesFragment"

class MyRidesFragment : Fragment(), RideSection.ClickListener {

    private lateinit var rideViewModel: RideViewModel
    private lateinit var ridesRecyclerView: RecyclerView
    private var callbacks : Callbacks? = null

    interface Callbacks {
        fun onRideSelected()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rideViewModel = ViewModelProvider(requireActivity()).get(RideViewModel::class.java)
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
            ridesMap = LoadDatesUseCase(requireContext(), rideItems).execute()
            for ((key, value) in ridesMap.entries) { // For every map item, add a section
                if (value.isNotEmpty()) {
                    sectionedAdapter.addSection(RideSection(requireContext(), key, value, this))
                }
            }
            ridesRecyclerView.adapter = sectionedAdapter
        })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onItemRootViewClicked(ride: Ride) {
        rideViewModel.setRide(ride) // Set the ride that the user clicked
        callbacks?.onRideSelected()
    }

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







