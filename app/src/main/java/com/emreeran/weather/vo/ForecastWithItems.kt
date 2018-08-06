package com.emreeran.weather.vo

import com.emreeran.weather.db.entity.Forecast
import com.emreeran.weather.db.entity.ForecastDay

/**
 * Created by Emre Eran on 3.08.2018.
 */
data class ForecastWithItems(
        val forecast: Forecast,
        var days: List<ForecastDay>
)