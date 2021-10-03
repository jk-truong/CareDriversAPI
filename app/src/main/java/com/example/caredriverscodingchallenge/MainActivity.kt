package com.example.caredriverscodingchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caredriverscodingchallenge.fragments.MyRidesFragment
import com.example.caredriverscodingchallenge.fragments.RideDetailsFragment
import java.util.*

private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity(), MyRidesFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = MyRidesFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onRideSelected(tripId: Int) {
        val fragment = RideDetailsFragment.newInstance(tripId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }
}