package com.emreeran.weather.repository

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import com.emreeran.weather.AppExecutors
import com.emreeran.weather.api.MetaWeatherService
import com.emreeran.weather.api.vo.LocationSearchResponse
import com.emreeran.weather.db.dao.LocationDao
import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.vo.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Singleton
class LocationRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val locationDao: LocationDao,
        private val weatherService: MetaWeatherService
) {
    fun findNearestLocationByCoordinates(lat: Double, long: Double): LiveData<Resource<Location>> {
        return object : NetworkBoundResource<Location, List<LocationSearchResponse>>(appExecutors) {

            override fun saveCallResult(body: List<LocationSearchResponse>) {
                val locations = ArrayList<Location>()

                for (item in body) {
                    var parent: Location? = null
                    item.parent?.let { it ->
                        parent = Location(
                                woeId = it.woeId,
                                name = it.title,
                                type = it.locationType,
                                coordinates = it.latLong
                        )
                        locationDao.insertIfNotExists(parent!!)
                    }

                    val location = Location(
                            woeId = item.woeId,
                            parentId = parent?.woeId,
                            name = item.title,
                            type = item.locationType,
                            coordinates = item.latLong,
                            distance = item.distance
                    )

                    locations.add(location)
                }

                locationDao.insertAll(locations)
            }

            override fun shouldFetch(data: Location?): Boolean {
                data?.let {
                    val hourAgo = Date()
                    hourAgo.time = hourAgo.time - DateUtils.HOUR_IN_MILLIS
                    return it.createdAt.before(hourAgo)
                }
                return true
            }

            override fun loadFromDb() = locationDao.getNearestLocationAsLiveData()

            override fun createCall() = weatherService.searchLocationByCoordinates("$lat,$long")

        }.asLiveData()
    }
}