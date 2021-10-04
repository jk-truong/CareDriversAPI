# CareDriversAPIExample

Libraries used:
- androidx recyclerview
- androidx lifecycle extensions
- google gson
- squareup retrofit 2.9.0
- kotlinx coroutines
- luizgrp sectionedrecyclerview adapter
- google play services maps

Link to APK: 
https://github.com/jk-truong/CareDriversAPIExample/blob/master/app/release/app-release.apk

Notes: 
  I went ahead and implemented the mapview even though it was optional, there are some 
issues with it crashing the fragment on rotation so I have forced the ride details 
fragment to be in portrait only.

Some improvements: 
- Touch up the UI.
- Fix the rotation  bugs with the mapview. 
- Maybe there is a better way to access the ride object for the ride detailsfragment, right now I 
  have it iterating through the whole list of rides until the tripId matches.
- There's some deprecated stuff that I would like to update. 
- Clean up some of the code in the RideDetailsFragment (adding the map functions made it messy).
