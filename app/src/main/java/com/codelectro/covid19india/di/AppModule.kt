package com.codelectro.covid19india.di

import android.content.Context
import androidx.room.Room
import com.codelectro.covid19india.db.MainDao
import com.codelectro.covid19india.db.MainDatabase
import com.codelectro.covid19india.entity.States
import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.repository.MainRepository
import com.codelectro.covid19india.util.Constants
import com.codelectro.covid19india.util.Constants.Companion.MAIN_DATABASE_NAME
import com.codelectro.covid19india.util.DataParser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideMainDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MainDatabase::class.java,
        MAIN_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideMainDao(db: MainDatabase) = db.getMainDao()

    @Singleton
    @Provides
    fun provideMainRepository(
        mainApi: MainApi,
        mainDao: MainDao
    ): MainRepository {
        return MainRepository(mainApi, mainDao)
    }

}