package com.emreeran.weather.di

import android.app.Application
import com.emreeran.weather.WeatherApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ConfigModule::class,
    ApiModule::class,
    DbModule::class,
    ActivityModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: WeatherApplication)
}