package com.example.caredriverscodingchallenge

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TripCard(@PrimaryKey val id: UUID = UUID.randomUUID(),
                    var timeRange: String = "",
                    var numRiders: String = "",
                    var estPrice: String = "",)