package com.emreeran.weather.ui

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
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

    lateinit var locationSearchViewModel: LocationSearchViewModel

    var binding by autoCleared<LocationSearchFragmentBinding>()

    private var searchKeyword = ""
    private var lastInput: Long = 0
    private val minimumSearchCharLength = 3
    private val inputHandler = Handler()
    private val inputWaitDelay: Long = 1000

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

        binding.search.addTextChangedListener(onSearchTextChanged)
    }

    private val searchIfInputFinished = Runnable {
        if (System.currentTimeMillis() > (lastInput + inputWaitDelay - 500)) {
            locationSearchViewModel.setQueryString(searchKeyword)
        }
    }

    private val onSearchTextChanged = object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            inputHandler.removeCallbacks(searchIfInputFinished)
        }

        override fun afterTextChanged(text: Editable?) {
            text?.let {
                if (it.length > minimumSearchCharLength) {
                    searchKeyword = it.toString()
                    lastInput = System.currentTimeMillis()
                    inputHandler.postDelayed(searchIfInputFinished, inputWaitDelay)
                }
            }
        }
    }
}