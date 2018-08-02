package com.emreeran.weather.di

import dagger.Module
import dagger.Provides

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
class ConfigModule {

    @Provides
    fun provideMetaWeatherApiInfo(): MetaWeatherApiInfo {
        return MetaWeatherApiInfo
    }

    @Provides
    fun provideDatabaseInfo(): DatabaseInfo {
        return DatabaseInfo
    }
}

object MetaWeatherApiInfo {
    const val url: String = "https://www.metaweather.com/api/"
}

object DatabaseInfo {
    const val name: String = "meta-weather.db"
}