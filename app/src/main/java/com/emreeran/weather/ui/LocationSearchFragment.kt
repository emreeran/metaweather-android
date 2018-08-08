package com.emreeran.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.emreeran.weather.R
import com.emreeran.weather.databinding.LocationSearchFragmentBinding
import com.emreeran.weather.di.Injectable
import com.emreeran.weather.util.autoCleared

/**
 * Created by Emre Eran on 8.08.2018.
 */
class LocationSearchFragment : Fragment(), Injectable {

    var binding by autoCleared<LocationSearchFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.location_search_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}