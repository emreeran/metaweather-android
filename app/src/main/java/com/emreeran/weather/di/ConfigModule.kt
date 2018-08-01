package com.emreeran.weather.di

import dagger.Module

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
class ConfigModule {
    fun provideMetaWeatherApiInfo(): MetaWeatherApiInfo {
        return MetaWeatherApiInfo
    }
}

object MetaWeatherApiInfo {
    const val url: String = "https://www.metaweather.com/api/"
}

object DatabaseInfo {
    const val name: String = "meta-weather.db"
}