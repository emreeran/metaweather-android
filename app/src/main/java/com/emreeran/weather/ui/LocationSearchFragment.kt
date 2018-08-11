package com.emreeran.weather.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.emreeran.weather.AppExecutors
import com.emreeran.weather.MainActivity
import com.emreeran.weather.R
import com.emreeran.weather.binding.FragmentDataBindingComponent
import com.emreeran.weather.databinding.LocationSearchFragmentBinding
import com.emreeran.weather.di.Injectable
import com.emreeran.weather.util.autoCleared
import javax.inject.Inject

/**
 * Created by Emre Eran on 8.08.2018.
 */
class LocationSearchFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var locationSearchViewModel: LocationSearchViewModel

    var binding by autoCleared<LocationSearchFragmentBinding>()

    private var adapter by autoCleared<LocationAdapter>()
    private var searchKeyword = ""
    private var lastInput: Long = 0
    private val minimumSearchCharLength = 3
    private val inputHandler = Handler()
    private val inputWaitDelay: Long = 1000

    private val dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.hide()
        binding = DataBindingUtil
                .inflate(inflater, R.layout.location_search_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationSearchViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocationSearchViewModel::class.java)

        val query = locationSearchViewModel.locationQuery
        query.observe(this, Observer { resource ->
            resource.data?.let {
                adapter.submitList(it.locations)
            } ?: kotlin.run {
                adapter.submitList(emptyList())
            }
        })

        binding.search.addTextChangedListener(onSearchTextChanged)

        adapter = LocationAdapter(dataBindingComponent, appExecutors) {
            dismissKeyboard(binding.root.windowToken)
            val action = LocationSearchFragmentDirections.showForecast(null, null)
            action.setLocationId(it.woeId)
            findNavController(this).navigate(action)
        }
        binding.resultList.adapter = adapter
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

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}