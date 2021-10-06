package com.example.caredriverscodingchallenge

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class ParseJsonStuffTest : TestCase() {
    private lateinit var parseJsonStuff: ParseJsonStuff
    private lateinit var rideItem: List<Ride>
    // androidx.test.core.app.ApplicationProvider.getApplicationContext()

    @Before
    fun setup() {
        parseJsonStuff = ParseJsonStuff()
    }

    @Test
    fun testGetEstimatedEarnings_returnsString() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        //val result = parseJsonStuff.getEstimatedEarnings()
    }

    @Test
    fun testGetHeaderDateRange() {}

    @Test
    fun testGetTimeString() {}

    @Test
    fun testGetNumPassengersString() {}

    @Test
    fun testGetOrderedWaypointsString() {}

    @Test
    fun testGetFormattedDate() {}
}