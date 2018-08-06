package com.emreeran.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emreeran.weather.db.dao.ForecastDao
import com.emreeran.weather.db.dao.LocationDao
import com.emreeran.weather.db.entity.Forecast
import com.emreeran.weather.db.entity.ForecastDay
import com.emreeran.weather.db.entity.Location

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Database(
        entities = [
            Location::class,
            Forecast::class,
            ForecastDay::class
        ],
        version = 1
)
@TypeConverters(Converters::class)
abstract class WeatherDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    abstract fun forecastDao(): ForecastDao
}