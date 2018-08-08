package com.emreeran.weather.di

import com.emreeran.weather.ui.ForecastFragment
import com.emreeran.weather.ui.LocationSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeForecastFragment(): ForecastFragment

    @ContributesAndroidInjector
    internal abstract fun contributeLocationSearchFragment(): LocationSearchFragment
}