package com.emreeran.weather.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 8.08.2018.
 */
@Entity(tableName = "location_query")
data class LocationQuery(
        @PrimaryKey val queryString: String,
        val locationIds: List<Int>,
        val createdAt: Date = Date()
)