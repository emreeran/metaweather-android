package com.emreeran.weather.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.emreeran.weather.api.vo.Coordinates
import com.emreeran.weather.api.vo.LocationType
import java.util.*

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Entity(tableName = "locations")
data class Location(
        @PrimaryKey val woeId: Int,
        @ForeignKey(entity = Location::class, parentColumns = ["woeId"], childColumns = ["parentId"]) val parentId: Int? = null,
        val name: String,
        val type: LocationType,
        val coordinates: Coordinates,
        val distance: Int? = null,
        val timeZone: TimeZone? = null,
        val createdAt: Date = Date()
)
