package com.emreeran.weather.db

import androidx.room.TypeConverter
import com.emreeran.weather.api.vo.Coordinates
import com.emreeran.weather.api.vo.LocationType
import timber.log.Timber
import java.util.*

/**
 * Created by Emre Eran on 2.08.2018.
 */
object Converters {
    @TypeConverter
    @JvmStatic
    fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    @JvmStatic
    fun dateToLong(date: Date?): Long? = date?.time

    @TypeConverter
    @JvmStatic
    fun toTimeZone(id: String?): TimeZone? = id?.let { TimeZone.getTimeZone(it) }

    @TypeConverter
    @JvmStatic
    fun timeZoneToString(timeZone: TimeZone?): String? = timeZone?.id

    @TypeConverter
    @JvmStatic
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
    @JvmStatic
    fun coordinatesToString(locationCoordinates: Coordinates?): String? =
            locationCoordinates?.let {
                "${it.latitude},${it.longitude}"
            }

    @TypeConverter
    @JvmStatic
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
    @JvmStatic
    fun locationTypeToString(locationType: LocationType?): String? = locationType?.typeName

    @TypeConverter
    @JvmStatic
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toInt()
                } catch (ex: NumberFormatException) {
                    Timber.e(ex, "Cannot convert $it to number")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }
}