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
  ~~I went ahead and implemented the mapview even though it was optional, there are some 
issues with it crashing the fragment on rotation so I have forced the ride details 
fragment to be in portrait only.~~ Fixed it by adding a landscape layout variation.

Some improvements: 
- Touch up the UI.
- ~~Fix the rotation bugs with the mapview.~~
- There's some deprecated stuff that I would like to update. 
- Clean up some of the code in the RideDetailsFragment (adding the map functions made it messy).
- Add Unit tests
- A few other things that I should fix but don't know that I should fix.
