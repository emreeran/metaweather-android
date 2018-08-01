package com.emreeran.weather.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Entity(tableName = "locations")
data class Location(
        @PrimaryKey val woeId: Int,
        @ForeignKey(entity = Location::class, parentColumns = ["woeId"], childColumns = ["parentId"]) val parentId: Int?,
        val name: String,
        val type: LocationType,
        val coordinates: LocationCoordinates,
        val timeZone: TimeZone?,
        val createdAt: Date
)

data class LocationCoordinates(
        val latitude: Double,
        val longitude: Double
)

enum class LocationType(name: String) {
    CITY("City"),
    REGION_STATE_PROVINCE("Region / State / Province"),
    COUNTRY("Country"),
    CONTINENT("Continent");

    val typeName: String = name
}