package com.codelectro.covid19india

import android.app.Application
import com.codelectro.covid19india.di.AppComponent
import com.codelectro.covid19india.di.DaggerAppComponent

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent
            .builder()
            .application(this).build()
    }
}