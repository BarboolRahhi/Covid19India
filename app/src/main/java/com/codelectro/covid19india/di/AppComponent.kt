package com.codelectro.covid19india.di

import android.app.Application
import com.codelectro.covid19india.di.main.MainComponent
import dagger.BindsInstance
import dagger.Component

@Component( modules = [AppModule::class, SubComponentModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun mainComponent(): MainComponent.Factory
}