package com.codelectro.covid19india.di

import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @JvmStatic
    @Provides
    @Named("dataRetro")
    fun provideRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Singleton
    @JvmStatic
    @Provides
    @Named("graphRetro")
    fun provideRetrofitGraphBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_GRAPH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    
}