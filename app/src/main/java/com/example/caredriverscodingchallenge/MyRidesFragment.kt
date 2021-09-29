package com.example.caredriverscodingchallenge

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
    private lateinit var myRidesRecyclerView: RecyclerView
    private var adapter: TripCardAdapter? = TripCardAdapter(emptyList())

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

        myRidesRecyclerView = view.findViewById(R.id.recycler_view_my_rides)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        myRidesRecyclerView.adapter = adapter

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

    private fun updateUI(tripCards: List<TripCard>) {
        adapter?.let {
            it.tripCards = tripCards
        } ?: run {
            adapter = TripCardAdapter(tripCards)
        }

        myRidesRecyclerView.adapter = adapter
    }

    private inner class TripCardHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
            private lateinit var tripCard: TripCard

            private val timeRange: TextView = itemView.findViewById(R.id.text_card_time_range)
            private val numRiders: TextView = itemView.findViewById(R.id.text_card_num_riders)
            private val estPrice: TextView = itemView.findViewById(R.id.text_card_estimated_price)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(tripCard: TripCard) {
                this.tripCard = tripCard
                timeRange.text = this.tripCard.timeRange
                numRiders.text = this.tripCard.numRiders
                estPrice.text = this.tripCard.estPrice
            }

            override fun onClick(v: View) {
                callbacks?.onCardSelected(tripCard.id)
            }
        }

    private inner class TripCardAdapter(var tripCards: List<TripCard>) :
        RecyclerView.Adapter<TripCardHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripCardHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_trip_card, parent, false)
            return TripCardHolder(view)
        }

        override fun onBindViewHolder(holder: TripCardHolder, position: Int) {
            val tripCard = tripCards[position]
            holder.bind(tripCard)
        }

        override fun getItemCount() = tripCards.size
    }

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}







