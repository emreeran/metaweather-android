package com.emreeran.weather.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Emre Eran on 3.08.2018.
 */
@Entity(tableName = "forecast_days", indices = [(Index("forecastId", "date", unique = true))])
data class ForecastDay(
        @PrimaryKey val id: Long,
        @ForeignKey(entity = Forecast::class, parentColumns = ["id"], childColumns = ["forecastId"]) val forecastId: Int,
        val stateName: String,
        val windDirection: String,
        val date: Date,
        val createdAt: Date,
        val maxTemp: Double,
        val minTemp: Double,
        val currentTemp: Double,
        val windSpeed: Double,
        val windDirectionDegrees: Double,
        val airPressure: Double,
        val humidity: Int,
        val visibility: Double,
        val predictability: Int
)