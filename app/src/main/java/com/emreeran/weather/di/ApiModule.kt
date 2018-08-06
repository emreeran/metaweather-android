package com.emreeran.weather.di

import com.emreeran.weather.BuildConfig
import com.emreeran.weather.api.LiveDataCallAdapterFactory
import com.emreeran.weather.api.MetaWeatherService
import com.emreeran.weather.api.vo.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideMetaWeatherService(info: MetaWeatherApiInfo): MetaWeatherService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor).build()

        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(LocationType::class.java, LocationTypeDeserializer())
        gsonBuilder.registerTypeAdapter(Coordinates::class.java, CoordinatesDeserializer())
        gsonBuilder.registerTypeAdapter(TimeZone::class.java, TimezoneDeserializer())
        val gson = gsonBuilder.create()

        return Retrofit.Builder()
                .baseUrl(info.url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(client)
                .build()
                .create(MetaWeatherService::class.java)
    }
}