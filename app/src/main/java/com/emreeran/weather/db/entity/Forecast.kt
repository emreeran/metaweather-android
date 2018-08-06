package com.emreeran.weather.db.entity

import androidx.room.*
import java.util.*

/**
 * Created by Emre Eran on 3.08.2018.
 */
@Entity(tableName = "forecasts", indices = [(Index("time", "locationId", unique = true))])
data class Forecast(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ForeignKey(entity = Location::class, parentColumns = ["woeId"], childColumns = ["locationId"]) val locationId: Int,
        val time: Date,
        val sunrise: Date,
        val sunset: Date
)