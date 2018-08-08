package com.emreeran.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.emreeran.weather.repository.LocationRepository
import com.emreeran.weather.vo.LocationQueryWithLocations
import com.emreeran.weather.vo.Resource
import javax.inject.Inject

/**
 * Created by Emre Eran on 8.08.2018.
 */
class LocationSearchViewModel @Inject constructor(
        locationRepository: LocationRepository
) : ViewModel() {
    private val queryString = MutableLiveData<String>()

    val locationQuery: LiveData<Resource<LocationQueryWithLocations>> = Transformations
            .switchMap(queryString) {
                locationRepository.searchLocationsByName(it)
            }

    fun setQueryString(query: String) {
        if (queryString.value == query) {
            return
        }
        queryString.value = query
    }
}