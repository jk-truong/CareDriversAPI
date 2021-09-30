package com.example.caredriverscodingchallenge

import com.google.gson.annotations.SerializedName

class Ride {
    @SerializedName("trip_id")
    var tripId: Int? = null

    @SerializedName("in_series")
    var inSeries: Boolean? = null

    @SerializedName("starts_at")
    var startsAt: String? = null

    @SerializedName("ends_at")
    var endsAt: String? = null

    @SerializedName("estimated_earnings_cents")
    var estimatedEarningsCents: Int? = null

    @SerializedName("estimated_ride_minutes")
    var estimatedRideMinutes: Int? = null

    @SerializedName("estimated_ride_miles")
    var estimatedRideMiles: Double? = null

    @SerializedName("ordered_waypoints")
    var orderedWaypoints: List<OrderedWaypoint>? = null

    inner class Location {
        @SerializedName("address")
        var address: String? = null

        @SerializedName("lat")
        var lat: Double? = null

        @SerializedName("lng")
        var lng: Double? = null
    }

    inner class Passenger {
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("booster_seat")
        var boosterSeat: Boolean? = null

        @SerializedName("first_name")
        var firstName: String? = null
    }

    inner class OrderedWaypoint {
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("anchor")
        var anchor: Boolean? = null

        @SerializedName("passengers")
        var passengers: List<Passenger>? = null

        @SerializedName("location")
        var location: android.location.Location? = null
    }
}