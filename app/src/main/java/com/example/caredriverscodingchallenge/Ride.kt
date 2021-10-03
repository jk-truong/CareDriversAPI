package com.example.caredriverscodingchallenge.adapters

import com.google.gson.annotations.SerializedName

/** This data class is the model for Ride */
data class Ride(
    @SerializedName("trip_id") var tripId: Int,
    @SerializedName("in_series") var inSeries: Boolean,
    @SerializedName("starts_at") var startsAt: String,
    @SerializedName("ends_at") var endsAt: String,
    @SerializedName("estimated_earnings_cents") var estimatedEarningsCents: Int,
    @SerializedName("estimated_ride_minutes") var estimatedRideMinutes: Int,
    @SerializedName("estimated_ride_miles") var estimatedRideMiles: Double,
    @SerializedName("ordered_waypoints") var orderedWaypoints: List<OrderedWaypoint>
)

data class OrderedWaypoint(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("anchor") var anchor: Boolean = false,
    @SerializedName("passengers") var passengers: List<Passenger> = emptyList(),
    @SerializedName("location") var location: Location = Location()
)

data class Passenger(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("booster_seat") var boosterSeat: Boolean = false,
    @SerializedName("first_name") var firstName: String = ""
)

data class Location(
    @SerializedName("address") var address: String = "",
    @SerializedName("lat") var lat: Double = 0.0,
    @SerializedName("lng") var lng: Double = 0.0
)

