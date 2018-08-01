package com.emreeran.weather.api.vo

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Created by Emre Eran on 2.08.2018.
 */
data class LocationSearchResponse(
        val title: String,
        @SerializedName("location_type")
        val locationType: LocationType,
        @SerializedName("latt_long")
        val latLong: Coordinates,
        @SerializedName("woeid")
        val woeId: Int,
        val distance: Int?
)

data class Coordinates(
        val latitude: Double,
        val longitude: Double
) {

    companion object {
        fun deserialize(value: String): Coordinates {
            val latLong = value.split(",")
            if (latLong.size == 2) {
                try {
                    val latitude = latLong[0].toDouble()
                    val longitude = latLong[1].toDouble()
                    return Coordinates(latitude, longitude)
                } catch (e: NumberFormatException) {
                    Timber.e(e, "Could not parse location coordinates.")
                }
            }

            throw InvalidCoordinateFormatException(value)
        }
    }
}

enum class LocationType {
    CITY,
    REGION_STATE_PROVINCE,
    COUNTRY,
    CONTINENT
}

class LocationTypeDeserializer : JsonDeserializer<LocationType> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocationType {
        json?.let {
            return when (it.asString) {
                "City" -> LocationType.CITY
                "Region / State / Province" -> LocationType.REGION_STATE_PROVINCE
                "Country" -> LocationType.COUNTRY
                "Continent" -> LocationType.CONTINENT
                else -> throw UnknownLocationTypeException(it.asString)
            }
        }

        throw EmptyLocationTypeResponseException()
    }
}

class CoordinatesDeserializer : JsonDeserializer<Coordinates> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Coordinates {
        json?.let {
            return Coordinates.deserialize(it.asString)
        }

        throw EmptyCoordinateResponseException()
    }
}

class EmptyLocationTypeResponseException : Exception()
class UnknownLocationTypeException(type: String) : Exception("Unknown location type $type")
class EmptyCoordinateResponseException : Exception()
class InvalidCoordinateFormatException(coordinates: String) : Exception("Invalid coordinates format $coordinates")
