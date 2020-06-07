package com.codelectro.covid19india.di.main

import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.main.state.DistrictFragment
import com.codelectro.covid19india.ui.main.state.StateFragment
import com.codelectro.covid19india.ui.main.total.TotalFragment
import dagger.Subcomponent

@Subcomponent( modules = [MainModule::class, MainViewModelModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(totalFragment: TotalFragment)
    fun inject(stateFragment: StateFragment)
    fun inject(districtFragment: DistrictFragment)
}