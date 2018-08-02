package com.emreeran.weather.util

import androidx.lifecycle.LiveData

/**
 * Created by Emre Eran on 3.08.2018.
 */
class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}
