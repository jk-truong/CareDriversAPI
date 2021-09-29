package com.example.caredriverscodingchallenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Ride {
    @SerializedName("trip_id")
    @Expose
    var tripId: Int? = null

    @SerializedName("in_series")
    @Expose
    var inSeries: Boolean? = null

    @SerializedName("starts_at")
    @Expose
    var startsAt: String? = null

    @SerializedName("ends_at")
    @Expose
    var endsAt: String? = null

    @SerializedName("estimated_earnings_cents")
    @Expose
    var estimatedEarningsCents: Int? = null

    @SerializedName("estimated_ride_minutes")
    @Expose
    var estimatedRideMinutes: Int? = null

    @SerializedName("estimated_ride_miles")
    @Expose
    var estimatedRideMiles: Double? = null

    @SerializedName("ordered_waypoints")
    @Expose
    var orderedWaypoints: List<OrderedWaypoint>? = null
}