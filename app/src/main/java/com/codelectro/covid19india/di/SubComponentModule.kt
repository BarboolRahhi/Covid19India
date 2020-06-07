package com.codelectro.covid19india.di

import com.codelectro.covid19india.di.main.MainComponent
import com.codelectro.covid19india.network.MainApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit

@Module( subcomponents = [MainComponent::class])
class SubComponentModule {

}