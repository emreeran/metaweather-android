package com.emreeran.weather.api

import com.emreeran.weather.api.vo.LocationSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Emre Eran on 1.08.2018.
 */
interface MetaWeatherService {
    @GET("api/location/search/")
    fun searchLocationByNameAsSingle(@Query("query") query: String): Single<List<LocationSearchResponse>>

    @GET("api/location/search/")
    fun searchLocationByCoordinatesAsSingle(@Query("lattlong") latLong: String): Single<List<LocationSearchResponse>>
}