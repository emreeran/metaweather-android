package com.emreeran.weather.ui

import androidx.navigation.fragment.NavHostFragment

/**
 * Created by Emre Eran on 8.09.2018.
 */
class SelectedLocationForecastFragment : ForecastFragment() {
    override fun getForecastData() {
        arguments?.let {
            val args = SelectedLocationForecastFragmentArgs.fromBundle(it)
            val locationId: Int = args.locationId

            if (locationId != -1) {
                forecastViewModel.setLocationId(locationId)
            }
        }
    }

    override fun navigateToSearch() {
        NavHostFragment.findNavController(this)
                .navigate(SelectedLocationForecastFragmentDirections.showSearchLocation())
    }
}