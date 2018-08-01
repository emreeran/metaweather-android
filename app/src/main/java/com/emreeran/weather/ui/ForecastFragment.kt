package com.emreeran.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emreeran.weather.R
import com.emreeran.weather.di.Injectable

/**
 * Created by Emre Eran on 2.08.2018.
 */
class ForecastFragment : Fragment(), Injectable {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.forecast_fragment, container, false)

}