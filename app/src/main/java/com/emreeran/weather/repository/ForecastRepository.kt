package com.emreeran.weather.repository

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emreeran.weather.AppExecutors
import com.emreeran.weather.api.MetaWeatherService
import com.emreeran.weather.api.vo.ApiResponse
import com.emreeran.weather.api.vo.ForecastResponse
import com.emreeran.weather.db.dao.ForecastDao
import com.emreeran.weather.db.entity.Forecast
import com.emreeran.weather.db.entity.ForecastDay
import com.emreeran.weather.vo.ForecastWithItems
import com.emreeran.weather.vo.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by Emre Eran on 3.08.2018.
 */
@Singleton
class ForecastRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val forecastDao: ForecastDao,
        private val weatherService: MetaWeatherService
) {
    fun getForecastByLocationId(locationId: Int): LiveData<Resource<ForecastWithItems>> {
        return object : NetworkBoundResource<ForecastWithItems, ForecastResponse>(appExecutors) {
            override fun saveCallResult(body: ForecastResponse) {
                val forecast = Forecast(
                        locationId = locationId,
                        time = body.time,
                        sunrise = body.sunrise,
                        sunset = body.sunset
                )
                val forecastId = forecastDao.insert(forecast)

                val forecastItems = ArrayList<ForecastDay>()
                for (item in body.forecastItems) {
                    val forecastItem = ForecastDay(
                            id = item.id,
                            forecastId = forecastId.toInt(),
                            stateName = item.stateName,
                            windDirection = item.windDirection,
                            date = item.date,
                            createdAt = item.createdAt,
                            maxTemp = item.maxTemp,
                            minTemp = item.minTemp,
                            currentTemp = item.currentTemp,
                            windSpeed = item.windSpeed,
                            windDirectionDegrees = item.windDirectionInDegrees,
                            airPressure = item.airPressure,
                            humidity = item.humidity,
                            predictability = item.predictability,
                            visibility = item.visibility
                    )

                    forecastItems.add(forecastItem)
                }
                forecastDao.insertForecastDays(forecastItems)
            }

            override fun shouldFetch(data: ForecastWithItems?): Boolean {
                data?.let {
                    val hourAgo = Date()
                    hourAgo.time = hourAgo.time - DateUtils.HOUR_IN_MILLIS
                    return it.forecast.time.before(hourAgo)
                }
                return true
            }

            override fun loadFromDb(): LiveData<ForecastWithItems> {
                val result = MutableLiveData<ForecastWithItems>()
                appExecutors.diskIO().execute {
                    val data = forecastDao.getLatestForecastByLocationWithDays(locationId)
                    result.postValue(data)
                }
                return result
            }

            override fun createCall(): LiveData<ApiResponse<ForecastResponse>> =
                    weatherService.getForecastByLocationId(locationId)
        }.asLiveData()
    }
}