package com.example.caredriverscodingchallenge

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class MyRidesFragment : Fragment() {

    private lateinit var myRidesRecyclerView: RecyclerView

    companion object {
        fun newInstance(): MyRidesFragment {
            return MyRidesFragment()
        }
    }
}