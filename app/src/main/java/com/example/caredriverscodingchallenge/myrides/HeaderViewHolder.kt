package com.example.caredriverscodingchallenge.myrides

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.R

/** Holder for the header. Needs to be displayed for every day (section) that has rides. */
class HeaderViewHolder(itemLayout: ConstraintLayout):
    RecyclerView.ViewHolder(itemLayout) {
    val headerDate: TextView = itemView.findViewById(R.id.text_header_date)
    val headerTimeRange: TextView = itemView.findViewById(R.id.text_header_time_range)
    val headerEstPrice: TextView = itemView.findViewById(R.id.text_header_estimated_price)
}