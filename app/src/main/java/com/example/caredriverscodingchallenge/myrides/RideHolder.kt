package com.example.caredriverscodingchallenge

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.myrides.Ride
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

/** Holder for the ride card */
class RideHolder(itemLayout: ConstraintLayout)
    : RecyclerView.ViewHolder(itemLayout),
    View.OnClickListener {
    val timeStart: TextView = itemView.findViewById(R.id.text_card_starts_at)
    val timeEnd: TextView = itemView.findViewById(R.id.text_card_ends_at)
    val estPrice: TextView = itemView.findViewById(R.id.text_card_estimated_price)
    val numRiders: TextView = itemView.findViewById(R.id.text_card_num_passengers)
    val orderedWaypoints: TextView = itemView.findViewById(R.id.text_card_ordered_waypoints)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        // TODO: Create intent, open new fragment
    }
}

/*
internal class ContactsSection(
    private val title: String, list: List<Contact>,
    clickListener: ClickListener
) :
    Section(
        SectionParameters.builder()
            .itemResourceId(R.layout.section_ex1_item)
            .headerResourceId(R.layout.section_ex1_header)
            .build()
    ) {
    private val list: List<Contact>
    private val clickListener: ClickListener
    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder: ItemViewHolder = holder as ItemViewHolder
        val contact: Contact = list[position]
        itemHolder.tvItem.setText(contact.name)
        itemHolder.imgItem.setImageResource(contact.profileImage)
        itemHolder.rootView.setOnClickListener { v ->
            clickListener.onItemRootViewClicked(
                this,
                itemHolder.getAdapterPosition()
            )
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder: HeaderViewHolder = holder as HeaderViewHolder
        headerHolder.tvTitle.setText(title)
    }

    internal interface ClickListener {
        fun onItemRootViewClicked(section: ContactsSection, itemAdapterPosition: Int)
    }

    init {
        this.list = list
        this.clickListener = clickListener
    }
}*/
