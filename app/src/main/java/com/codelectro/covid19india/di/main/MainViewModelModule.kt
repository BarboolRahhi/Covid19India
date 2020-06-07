package com.codelectro.covid19india.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codelectro.covid19india.ui.main.state.StateViewModel
import com.codelectro.covid19india.ui.main.total.TotalViewModel
import com.codelectro.covid19india.viemodels.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @MainViewModelKey(TotalViewModel::class)
    abstract fun bindTotalViewModel(viewModel: TotalViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(StateViewModel::class)
    abstract fun bindStateViewModel(viewModel: StateViewModel): ViewModel
}