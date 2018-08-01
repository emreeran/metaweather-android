package com.emreeran.weather.di

import com.emreeran.weather.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}