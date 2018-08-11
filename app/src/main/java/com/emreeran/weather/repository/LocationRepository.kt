package com.emreeran.weather.repository

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emreeran.weather.AppExecutors
import com.emreeran.weather.api.MetaWeatherService
import com.emreeran.weather.api.vo.LocationSearchResponse
import com.emreeran.weather.db.dao.LocationDao
import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.db.entity.LocationQuery
import com.emreeran.weather.vo.LocationQueryWithLocations
import com.emreeran.weather.vo.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Singleton
class LocationRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val locationDao: LocationDao,
        private val weatherService: MetaWeatherService
) {
    fun findLocationById(id: Int): LiveData<Resource<Location>> {
        val result = MutableLiveData<Resource<Location>>()
        appExecutors.diskIO().execute {
            val data = locationDao.findLocationByIdSync(id)
            result.postValue(Resource.success(data))
        }

        return result
    }

    fun findNearestLocationByCoordinates(lat: Double, long: Double): LiveData<Resource<Location>> {
        return object : NetworkBoundResource<Location, List<LocationSearchResponse>>(appExecutors) {

            override fun saveCallResult(body: List<LocationSearchResponse>) {
                saveLocations(body)
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

    fun searchLocationsByName(queryString: String): LiveData<Resource<LocationQueryWithLocations>> {
        return object : NetworkBoundResource<LocationQueryWithLocations, List<LocationSearchResponse>>(appExecutors) {
            override fun saveCallResult(body: List<LocationSearchResponse>) {
                val ids = saveLocations(body)
                val query = LocationQuery(queryString, ids)
                locationDao.insertQuery(query)
            }

            override fun shouldFetch(data: LocationQueryWithLocations?): Boolean {
                data?.query?.let {
                    val hourAgo = Date()
                    hourAgo.time = hourAgo.time - DateUtils.HOUR_IN_MILLIS
                    return it.createdAt.before(hourAgo)
                }
                return true
            }

            override fun loadFromDb(): LiveData<LocationQueryWithLocations> {
                val result = MutableLiveData<LocationQueryWithLocations>()
                appExecutors.diskIO().execute {
                    val data = locationDao.listQueryLocations(queryString)
                    result.postValue(data)
                }
                return result
            }

            override fun createCall() = weatherService.searchLocationByName(queryString)

        }.asLiveData()
    }

    private fun saveLocations(locationsResponse: List<LocationSearchResponse>): List<Int> {
        val locations = ArrayList<Location>()
        val ids = ArrayList<Int>()

        for (item in locationsResponse) {
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
            ids.add(location.woeId)
        }

        locationDao.insertAll(locations)
        return ids
    }
}