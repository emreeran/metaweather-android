package com.emreeran.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.emreeran.weather.R
import com.emreeran.weather.databinding.ForecastFragmentBinding
import com.emreeran.weather.db.entity.ForecastDay
import com.emreeran.weather.di.Injectable
import com.emreeran.weather.util.autoCleared
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Emre Eran on 2.08.2018.
 */
class ForecastFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var forecastViewModel: ForecastViewModel

    var binding by autoCleared<ForecastFragmentBinding>()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.forecast_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        forecastViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ForecastViewModel::class.java)

        binding.hourDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val location = forecastViewModel.location
        val forecast = forecastViewModel.forecast

        location.observe(this, Observer {
            binding.location = it?.data
        })


        forecast.observe(this, Observer {
            Timber.i("Forecast: ${it?.data}")
            it?.data?.let {
                binding.forecast = it.forecast
                setTodayForecast(it.days)
            }
        })

        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                val rxPermissions = RxPermissions(this)

                disposables.add(rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe { granted ->
                            Timber.i("Permission granted: $granted")
                            if (granted) {
                                context?.let {
                                    getUserLocation(it)
                                }

                            } else {
                                // TODO: show warning message
                            }
                        })
            } else {
                getUserLocation(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(context: Context) {
        val rxLocation = RxLocation(context)
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        disposables.add(rxLocation.location().updates(locationRequest)
                .subscribe { location ->
                    forecastViewModel.setUserCoordinates(location.latitude, location.longitude)
                })
    }

    private fun setTodayForecast(forecastDays: List<ForecastDay>) {
        for (item in forecastDays) {
            if (DateUtils.isToday(item.date.time)) {
                binding.today = item
            }
        }
    }
}