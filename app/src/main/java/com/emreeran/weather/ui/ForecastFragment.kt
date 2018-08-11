package com.emreeran.weather.ui

import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.emreeran.weather.MainActivity
import com.emreeran.weather.R
import com.emreeran.weather.databinding.ForecastFragmentBinding
import com.emreeran.weather.db.entity.ForecastDay
import com.emreeran.weather.di.Injectable
import com.emreeran.weather.util.autoCleared
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.show()
        binding = DataBindingUtil
                .inflate(inflater, R.layout.forecast_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        forecastViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ForecastViewModel::class.java)

        binding.hourDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.dayDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        binding.fullDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val location = forecastViewModel.location
        val forecast = forecastViewModel.forecast

        location.observe(this, Observer {
            binding.location = it?.data
        })

        forecast.observe(this, Observer { resource ->
            resource?.data?.let {
                binding.forecast = it.forecast
                setTodayForecast(it.days)
            }
        })

        arguments?.let {
            val args = ForecastFragmentArgs.fromBundle(it)
            val latitude: Double? = args.latitude?.toDouble()
            val longitude: Double? = args.longitude?.toDouble()
            val locationId: Int = args.locationId

            if (latitude != null && longitude != null) {
                forecastViewModel.setUserCoordinates(latitude, longitude)
            } else if (locationId != -1) {
                forecastViewModel.setLocationId(locationId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.forecast_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                findNavController(this)
                        .navigate(ForecastFragmentDirections.showSearchLocation())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTodayForecast(forecastDays: List<ForecastDay>) {
        for (item in forecastDays) {
            if (DateUtils.isToday(item.date.time)) {
                binding.today = item
            }
        }
    }
}