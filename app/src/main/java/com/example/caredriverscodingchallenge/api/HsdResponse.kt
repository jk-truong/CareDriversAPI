package com.example.caredriverscodingchallenge.api

import com.example.caredriverscodingchallenge.myrides.Ride

/* Maps to the outermost object in the JSON data */
class HsdResponse {
    lateinit var rides: List<Ride>
}