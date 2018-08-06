package com.emreeran.weather.api

import androidx.lifecycle.LiveData
import com.emreeran.weather.api.vo.ApiResponse
import com.emreeran.weather.api.vo.ForecastResponse
import com.emreeran.weather.api.vo.LocationSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Emre Eran on 1.08.2018.
 */
interface MetaWeatherService {
    @GET("api/location/search/")
    fun searchLocationByName(@Query("query") query: String): Call<List<LocationSearchResponse>>

    @GET("api/location/search/")
    fun searchLocationByCoordinates(@Query("lattlong") latLong: String): LiveData<ApiResponse<List<LocationSearchResponse>>>

    @GET("api/location/{id}/")
    fun getForecastByLocationId(@Path("id") id: Int): LiveData<ApiResponse<ForecastResponse>>
}