package com.example.caredriverscodingchallenge.myrides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.MainActivity
import com.example.caredriverscodingchallenge.R
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

private const val TAG = "MyRidesFragment"
private const val ACTION_BAR_TITLE = "My Rides"

class MyRidesFragment : Fragment(), RidesSection.ClickListener {

    private lateinit var rideViewModel: RideViewModel
    private lateinit var ridesRecyclerView: RecyclerView

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
            ridesMap = LoadDatesUseCase(rideItems).execute()
            for ((key, value) in ridesMap.entries) { // For every map item, add a section
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

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







