package com.emreeran.weather.di

import com.emreeran.weather.ui.LocationSearchFragment
import com.emreeran.weather.ui.SelectedLocationForecastFragment
import com.emreeran.weather.ui.UserLocationForecastFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeUserLocationForecastFragment(): UserLocationForecastFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSelectedLocationForecastFragment(): SelectedLocationForecastFragment

    @ContributesAndroidInjector
    internal abstract fun contributeLocationSearchFragment(): LocationSearchFragment
}