package com.example.caredriverscodingchallenge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.caredriverscodingchallenge.R
import com.example.caredriverscodingchallenge.viewholders.AddressViewHolder

class AddressAdapter(
    context: Context,
    layoutInflater: LayoutInflater,
    private val addressList: List<OrderedWaypoint>
) : RecyclerView.Adapter<AddressViewHolder>() {
    private val myContext = context
    private val myLayoutInflater = layoutInflater
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {
        val view = myLayoutInflater.inflate(
            R.layout.list_item_address,
            parent,
            false
        ) as ConstraintLayout
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val addressItem = addressList[position]
        val addressLine = addressItem.location.address
        var icon = ContextCompat.getDrawable(myContext, R.drawable.diamond)
        var anchorText = myContext.resources.getString(R.string.drop_off)
        if (addressItem.anchor) {
            icon = ContextCompat.getDrawable(myContext, R.drawable.circle)
            anchorText = myContext.resources.getString(R.string.pickup)
        }

        holder.anchorIcon.background = icon
        holder.anchorText.text = anchorText
        holder.addressLine.text = addressLine
    }

    override fun getItemCount(): Int = addressList.size
}