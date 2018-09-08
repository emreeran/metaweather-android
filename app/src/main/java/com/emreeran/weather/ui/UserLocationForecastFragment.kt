package com.emreeran.weather.ui

import android.Manifest
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.emreeran.locationlivedata.LocationLiveData
import com.emreeran.permissionlivedata.PermissionLiveData
import com.emreeran.permissionlivedata.Status
import timber.log.Timber

/**
 * Created by Emre Eran on 8.09.2018.
 */
class UserLocationForecastFragment : ForecastFragment() {
    override fun getForecastData() {
        context?.let { context ->
            val permissionLiveData = PermissionLiveData.create(this, Manifest.permission.ACCESS_FINE_LOCATION)
            permissionLiveData.observe(this, Observer { permission ->
                if (permission.status == Status.RECEIVED) {
                    if (permission.granted) {
                        Timber.d("Location permission granted.")
                        LocationLiveData.create(context, interval = 1000).observe(this, Observer { location ->
                            forecastViewModel.setUserCoordinates(location.latitude, location.longitude)
                        })
                    } else {
                        // TODO: show message
                    }
                } else {
                    Timber.d("Asked for location permission.")
                }
            })
        }
    }

    override fun navigateToSearch() {
        NavHostFragment.findNavController(this)
                .navigate(UserLocationForecastFragmentDirections.showSearchLocation())
    }
}