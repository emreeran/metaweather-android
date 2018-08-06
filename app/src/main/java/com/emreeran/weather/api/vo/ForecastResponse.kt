package com.emreeran.weather.api.vo

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Emre Eran on 3.08.2018.
 */
data class ForecastResponse(
        @SerializedName("consolidated_weather") val forecastItems: List<ForecastItem>,
        val time: Date,
        @SerializedName("sun_rise") val sunrise: Date,
        @SerializedName("sun_set") val sunset: Date,
        @SerializedName("timezone_name") val timezoneName: String,
        val parent: LocationSearchResponse?,
        val title: String,
        @SerializedName("location_type")
        val locationType: LocationType,
        @SerializedName("latt_long")
        val latLong: Coordinates,
        @SerializedName("woeid")
        val woeId: Int,
        val timezone: TimeZone
)

data class ForecastItem(
        val id: Long,
        @SerializedName("weather_state_name") val stateName: String,
        @SerializedName("weather_state_abbr") val stateAbbr: String,
        @SerializedName("wind_direction_compass") val windDirection: String,
        @SerializedName("created") val createdAt: Date,
        @SerializedName("applicable_date") val date: Date,
        @SerializedName("min_temp") val minTemp: Double,
        @SerializedName("max_temp") val maxTemp: Double,
        @SerializedName("the_temp") val currentTemp: Double,
        @SerializedName("wind_speed") val windSpeed: Double,
        @SerializedName("wind_direction") val windDirectionInDegrees: Double,
        @SerializedName("air_pressure") val airPressure: Double,
        val humidity: Int,
        val visibility: Double,
        val predictability: Int
)

class TimezoneDeserializer : JsonDeserializer<TimeZone> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TimeZone {
        json?.let {
            return TimeZone.getTimeZone(it.asString)
        }
        throw EmptyTimezoneException()
    }
}

class EmptyTimezoneException : Exception()