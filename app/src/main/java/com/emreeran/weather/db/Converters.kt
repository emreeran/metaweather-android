package com.emreeran.weather.db

import androidx.room.TypeConverter
import com.emreeran.weather.api.vo.Coordinates
import com.emreeran.weather.api.vo.LocationType
import timber.log.Timber
import java.util.*

/**
 * Created by Emre Eran on 2.08.2018.
 */
class Converters {
    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToLong(date: Date?): Long? = date?.time

    @TypeConverter
    fun toTimeZone(id: String?): TimeZone? = id?.let { TimeZone.getTimeZone(it) }

    @TypeConverter
    fun timeZoneToString(timeZone: TimeZone?): String? = timeZone?.id

    @TypeConverter
    fun toCoordinates(value: String?): Coordinates? {
        value?.let {
            val latLong = it.split(",")
            if (latLong.size == 2) {
                try {
                    val latitude = latLong[0].toDouble()
                    val longitude = latLong[1].toDouble()
                    return Coordinates(latitude, longitude)
                } catch (e: NumberFormatException) {
                    Timber.e(e, "Could not parse location coordinates.")
                }
            }
        }
        return null
    }

    @TypeConverter
    fun coordinatesToString(locationCoordinates: Coordinates?): String? =
            locationCoordinates?.let {
                "${it.latitude},${it.longitude}"
            }

    @TypeConverter
    fun toLocationType(value: String?): LocationType? {
        value?.let {
            return when (it) {
                "City" -> LocationType.CITY
                "Region / State / Province" -> LocationType.REGION_STATE_PROVINCE
                "Country" -> LocationType.COUNTRY
                "Continent" -> LocationType.CONTINENT
                else -> return null
            }
        }
        return null
    }

    @TypeConverter
    fun locationTypeToString(locationType: LocationType?): String? = locationType?.typeName

}