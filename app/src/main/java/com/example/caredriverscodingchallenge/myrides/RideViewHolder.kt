package com.example.caredriverscodingchallenge.myrides

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.R

/** Holder for the ride card */
class RideViewHolder(itemLayout: ConstraintLayout)
    : RecyclerView.ViewHolder(itemLayout) {
    val timeStart: TextView = itemView.findViewById(R.id.text_card_starts_at)
    val timeEnd: TextView = itemView.findViewById(R.id.text_card_ends_at)
    val estEarnings: TextView = itemView.findViewById(R.id.text_card_estimated_earnings)
    val numRiders: TextView = itemView.findViewById(R.id.text_card_num_passengers)
    val orderedWaypoints: TextView = itemView.findViewById(R.id.text_card_ordered_waypoints)
    val rootView: View = itemLayout
}