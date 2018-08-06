package com.emreeran.weather.di

import android.app.Application
import androidx.room.Room
import com.emreeran.weather.db.WeatherDb
import com.emreeran.weather.db.dao.ForecastDao
import com.emreeran.weather.db.dao.LocationDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: Application, databaseInfo: DatabaseInfo): WeatherDb =
            Room.databaseBuilder(app, WeatherDb::class.java, databaseInfo.name).build()


    @Singleton
    @Provides
    fun provideLocationDao(db: WeatherDb): LocationDao = db.locationDao()

    @Singleton
    @Provides
    fun provideForecastDao(db: WeatherDb): ForecastDao = db.forecastDao()
}