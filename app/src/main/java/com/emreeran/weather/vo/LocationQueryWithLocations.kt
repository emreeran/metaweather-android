package com.emreeran.weather.vo

import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.db.entity.LocationQuery

/**
 * Created by Emre Eran on 8.08.2018.
 */
data class LocationQueryWithLocations(
        val query: LocationQuery?,
        val locations: List<Location> = ArrayList()
)