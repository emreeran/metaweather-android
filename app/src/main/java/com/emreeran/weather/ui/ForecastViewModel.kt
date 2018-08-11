package com.emreeran.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.repository.ForecastRepository
import com.emreeran.weather.repository.LocationRepository
import com.emreeran.weather.util.AbsentLiveData
import com.emreeran.weather.vo.ForecastWithItems
import com.emreeran.weather.vo.Resource
import javax.inject.Inject

/**
 * Created by Emre Eran on 3.08.2018.
 */
class ForecastViewModel @Inject constructor(
        locationRepository: LocationRepository,
        forecastRepository: ForecastRepository
) : ViewModel() {

    private val locationInfo: MutableLiveData<LocationInfo> = MutableLiveData()

    val location: LiveData<Resource<Location>> = Transformations
            .switchMap(locationInfo) {
                if (it.lat != null && it.long != null) {
                    locationRepository.findNearestLocationByCoordinates(it.lat, it.long)
                } else if (it.id != null) {
                    locationRepository.findLocationById(it.id)
                } else {
                    AbsentLiveData.create()
                }
            }

    val forecast: LiveData<Resource<ForecastWithItems>> = Transformations
            .switchMap(location) {
                it.data?.let { location ->
                    forecastRepository.getForecastByLocationId(location.woeId)
                }
            }

    fun setUserCoordinates(lat: Double, long: Double) {
        val update = LocationInfo(lat, long)

        if (locationInfo.value == update) {
            return
        }

        locationInfo.value = update
    }

    fun setLocationId(id: Int) {
        val update = LocationInfo(id = id)

        if (locationInfo.value == update) {
            return
        }

        locationInfo.value = update
    }

    data class LocationInfo(
            val lat: Double? = null,
            val long: Double? = null,
            val id: Int? = null
    )
}