package com.emreeran.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.repository.LocationRepository
import com.emreeran.weather.util.AbsentLiveData
import com.emreeran.weather.vo.Resource
import javax.inject.Inject

/**
 * Created by Emre Eran on 3.08.2018.
 */
class ForecastViewModel @Inject constructor(locationRepository: LocationRepository) : ViewModel() {

    private val userCoordinates: MutableLiveData<UserCoordinates> = MutableLiveData()

    val location: LiveData<Resource<Location>> = Transformations
            .switchMap(userCoordinates) {
                it.ifExists { lat, long ->
                    locationRepository.findNearestLocationByCoordinates(lat, long)
                }
            }

    fun setUserCoordinates(lat: Double, long: Double) {
        val update = UserCoordinates(lat, long)

        if (userCoordinates.value == update) {
            return
        }

        userCoordinates.value = update
    }

    data class UserCoordinates(
            val lat: Double?,
            val long: Double?
    ) {
        fun <T> ifExists(f: (Double, Double) -> LiveData<T>): LiveData<T> {
            if (lat != null && long != null) {
                return f(lat, long)
            }
            return AbsentLiveData.create()
        }
    }
}