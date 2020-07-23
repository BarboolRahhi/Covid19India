package com.codelectro.covid19india.di.main

import android.app.Application
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.network.GraphApi
import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.ui.main.state.DistrictRecyclerAdapter
import com.codelectro.covid19india.ui.main.state.StateRecyclerAdapter
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Named

@Module
object MainModule {

    @ActivityScope
    @JvmStatic
    @Provides
    fun provideMainApi(@Named("dataRetro") retrofit: Retrofit): MainApi {
        return retrofit
            .create(MainApi::class.java)
    }

    @ActivityScope
    @JvmStatic
    @Provides
    fun provideMainGraphApi(@Named("graphRetro") retrofit: Retrofit): GraphApi {
        return retrofit
            .create(GraphApi::class.java)
    }


    @JvmStatic
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @JvmStatic
    @Provides
    fun provideStateAdapter(): StateRecyclerAdapter {
        return StateRecyclerAdapter()
    }

    @JvmStatic
    @Provides
    fun provideDistrictAdapter(): DistrictRecyclerAdapter {
        return DistrictRecyclerAdapter()
    }

    @JvmStatic
    @Provides
    fun provideLayoutManager(application: Application): RecyclerView.LayoutManager {
        return LinearLayoutManager(application)
    }


}