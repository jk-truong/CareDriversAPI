package com.example.caredriverscodingchallenge

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "MyRidesFragment"

class MyRidesFragment : Fragment() {

    private lateinit var myRidesViewModel: MyRidesViewModel
    private lateinit var ridesRecyclerView: RecyclerView

    private var callbacks : Callbacks? = null

    interface Callbacks {
        fun onCardSelected(cardId: UUID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        myRidesViewModel = ViewModelProviders.of(this).get(MyRidesViewModel::class.java)
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

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myRidesViewModel.myRidesItemLiveData.observe(
            viewLifecycleOwner,
            Observer { rideItems ->
                Log.d(TAG, "Have ride items from view model $rideItems")
                myRidesRecyclerView.adapter = RideAdapter(tripCards)
            })
    }*/

 /*   inner class RideAdapter(private val ride: List<Ride>) :
        RecyclerView.Adapter<RideAdapter.ViewHolder?>() {

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
        ): ViewHolder {
            val view: View = layoutInflater.inflate(
                R.layout.list_item_ride,
                viewGroup,
                false
                )
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = ride.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.timeStart.text = ride[position].startsAt
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val timeStart: TextView = itemView.findViewById<View>(R.id.text_card_time_range) as TextView
            val numRiders: TextView = itemView.findViewById<View>(R.id.text_card_num_riders) as TextView
            val estPrice: TextView = itemView.findViewById<View>(R.id.text_card_estimated_price) as TextView
            val orderedWaypoints: RecyclerView =
                itemView.findViewById(R.id.recycler_view_card_ordered_waypoints) as RecyclerView

        }



        *//* init {
             this.ride = ride
             this.context = context
         }*//*
    }*/

    class RideAdapter(private val context: Context, private val ride: ArrayList<Ride>) :
        RecyclerView.Adapter<RideAdapter.ViewHolder?>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_ride, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            viewHolder.timeStart.setText(ride[i].startsAt)
            ride[i].estimatedEarningsCents?.let { viewHolder.estPrice.setText(it) }
        }

        override fun getItemCount(): Int {
            return ride.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val timeStart: TextView = itemView.findViewById<View>(R.id.text_card_time_range) as TextView
            val estPrice: TextView = itemView.findViewById<View>(R.id.text_card_estimated_price) as TextView

        }
    }


    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







