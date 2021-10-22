package com.example.caredriverscodingchallenge

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class ParseJsonStuffTest : TestCase() {
    private var parseJsonStuff: ParseJsonStuff = ParseJsonStuff()
    private var rideItem: List<Ride> = mutableListOf(
        // Dummy data
        Ride(
            1,
            false,
            "2021-06-17T11:18:00Z",
            "2021-06-17T12:37:00Z",
            6071,
            50,
            18.48,
            mutableListOf(
                OrderedWaypoint(
                    222,
                    true,
                    mutableListOf(
                        Passenger(123, true, "Mark"),
                        Passenger(121, false, "John")
                    ),
                    Location(
                        "2565 E Underhill Ave, Anaheim 92806",
                        34.17006916353578,
                        -118.43274040504944
                    )
                )
            )
        )
    )

    @Test
    fun testGetEstimatedEarnings_returnsString() {
        rideItem[0].estimatedEarningsCents = 1000
        val result = parseJsonStuff.getEstimatedEarnings(rideItem)

        assertEquals("$10.00", result)
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