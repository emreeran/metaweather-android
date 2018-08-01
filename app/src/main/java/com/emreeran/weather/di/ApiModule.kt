package com.emreeran.weather.di

import com.emreeran.weather.api.MetaWeatherService
import com.emreeran.weather.api.vo.Coordinates
import com.emreeran.weather.api.vo.CoordinatesDeserializer
import com.emreeran.weather.api.vo.LocationType
import com.emreeran.weather.api.vo.LocationTypeDeserializer
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideMetaWeatherService(info: MetaWeatherApiInfo): MetaWeatherService {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(LocationType::class.java, LocationTypeDeserializer())
        gsonBuilder.registerTypeAdapter(Coordinates::class.java, CoordinatesDeserializer())
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
                .baseUrl(info.url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MetaWeatherService::class.java)
    }
}