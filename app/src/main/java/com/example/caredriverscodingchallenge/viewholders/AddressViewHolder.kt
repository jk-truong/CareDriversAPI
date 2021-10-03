package com.example.caredriverscodingchallenge.viewholders

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.R

class AddressViewHolder(itemLayout: ConstraintLayout):
    RecyclerView.ViewHolder(itemLayout) {
    val anchorIcon: ImageView = itemView.findViewById(R.id.img_anchor_type)
    val anchorText: TextView = itemView.findViewById(R.id.text_address_anchor)
    val addressLine: TextView = itemView.findViewById(R.id.text_address_line)
}