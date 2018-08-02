package com.emreeran.weather.api

import androidx.lifecycle.LiveData
import com.emreeran.weather.api.vo.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Emre Eran on 3.08.2018.
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?):
            CallAdapter<*, *>? {

        returnType?.let {
            if (CallAdapter.Factory.getRawType(it) != LiveData::class.java) {
                return null
            }
            val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType as ParameterizedType)
            val rawObservableType = CallAdapter.Factory.getRawType(observableType)
            if (rawObservableType != ApiResponse::class.java) {
                throw IllegalArgumentException("type must be a resource")
            }
            if (observableType !is ParameterizedType) {
                throw IllegalArgumentException("resource must be parameterized")
            }
            val bodyType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
            return LiveDataCallAdapter<Any>(bodyType)
        }
        return null
    }
}