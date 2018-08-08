package com.emreeran.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.emreeran.weather.R
import com.emreeran.weather.databinding.LocationSearchFragmentBinding
import com.emreeran.weather.di.Injectable
import com.emreeran.weather.util.autoCleared
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Emre Eran on 8.08.2018.
 */
class LocationSearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var binding by autoCleared<LocationSearchFragmentBinding>()

    lateinit var locationSearchViewModel: LocationSearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.location_search_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationSearchViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationSearchViewModel::class.java)

        val query = locationSearchViewModel.locationQuery
        query.observe(this, Observer {
            Timber.i(it.data.toString())
        })
    }
}