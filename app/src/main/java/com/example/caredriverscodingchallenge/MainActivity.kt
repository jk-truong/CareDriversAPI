package com.example.caredriverscodingchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caredriverscodingchallenge.myrides.MyRidesFragment

class MainActivity : AppCompatActivity() {
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

    fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }
}