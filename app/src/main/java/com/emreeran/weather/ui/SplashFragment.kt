package com.emreeran.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.emreeran.weather.MainActivity
import com.emreeran.weather.R
import com.emreeran.weather.di.Injectable
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Emre Eran on 11.08.2018.
 */
class SplashFragment : Fragment(), Injectable {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let { context ->
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                val rxPermissions = RxPermissions(this)

                disposables.add(rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe { granted ->
                            if (granted) {
                                getUserLocation(context)
                            } else {
                                findNavController(this)
                                        .navigate(SplashFragmentDirections.showSearchLocation())
                            }
                        })
            } else {
                getUserLocation(context)
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
                    findNavController(this).navigate(
                            SplashFragmentDirections
                                    .showForecast(location.latitude.toString(), location.longitude.toString())
                    )
                })
    }
}