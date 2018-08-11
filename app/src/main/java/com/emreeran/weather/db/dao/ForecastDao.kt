package com.emreeran.weather.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emreeran.weather.db.entity.Forecast
import com.emreeran.weather.db.entity.ForecastDay
import com.emreeran.weather.vo.ForecastWithItems

/**
 * Created by Emre Eran on 3.08.2018.
 */
@Dao
abstract class ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(forecast: Forecast): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertForecastDays(items: List<ForecastDay>)

    @Query("SELECT * FROM forecast_days WHERE forecastId = :id AND date >= :date ORDER BY date ASC")
    abstract fun listForecastDaysByForecastIdAfterDate(id: Int, date: Long): LiveData<List<ForecastDay>>

    @Query("SELECT * FROM forecasts WHERE locationId = :locationId ORDER BY time DESC LIMIT 1")
    abstract fun getLatestForecastByLocationId(locationId: Int): Forecast?

    @Query("SELECT * FROM forecast_days WHERE forecastId = :forecastId ORDER BY date ASC")
    abstract fun listForecastDaysByForecastId(forecastId: Int): List<ForecastDay>

    @Transaction
    open fun getLatestForecastByLocationWithDays(locationId: Int): ForecastWithItems? {
        val forecast = getLatestForecastByLocationId(locationId)
        forecast?.let {
            val items = listForecastDaysByForecastId(it.id)
            return ForecastWithItems(forecast, items)
        }
        return null
    }
}
