package com.emreeran.weather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.emreeran.weather.ui.ForecastViewModel
import com.emreeran.weather.ui.LocationSearchViewModel
import com.emreeran.weather.viewmodel.WeatherViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Emre Eran on 3.08.2018.
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    abstract fun bindForecastViewModel(forecastViewModel: ForecastViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationSearchViewModel::class)
    abstract fun bindLocationSearchViewModel(locationSearchViewModel: LocationSearchViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: WeatherViewModelFactory): ViewModelProvider.Factory
}