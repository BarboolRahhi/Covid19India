package com.codelectro.covid19india.di

import android.content.Context
import com.codelectro.covid19india.entity.States
import com.codelectro.covid19india.network.MainApi
import com.codelectro.covid19india.network.NetworkConnectionInterceptor
import com.codelectro.covid19india.util.Constants
import com.codelectro.covid19india.util.DataParser
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): GsonBuilder {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(States::class.java, DataParser())
        return gsonBuilder
    }

    @Singleton
    @Provides
    fun provideInternetInterceptor(
        @ApplicationContext app: Context
    ): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(app)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
       interceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
       return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        gsonBuilder: GsonBuilder,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .client(client)
            .build()

    }

    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit
            .create(MainApi::class.java)
    }


}