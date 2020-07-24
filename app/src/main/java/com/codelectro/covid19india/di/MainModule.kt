package com.codelectro.covid19india.di

import com.codelectro.covid19india.ui.main.state.DistrictRecyclerAdapter
import com.codelectro.covid19india.ui.main.state.StateRecyclerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainModule {


    @Provides
    fun provideStateAdapter(): StateRecyclerAdapter {
        return StateRecyclerAdapter()
    }

    @Provides
    fun provideDistrictAdapter(): DistrictRecyclerAdapter {
        return DistrictRecyclerAdapter()
    }


}