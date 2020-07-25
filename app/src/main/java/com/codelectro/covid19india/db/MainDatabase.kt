package com.codelectro.covid19india.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codelectro.covid19india.entity.CasesSeries
import com.codelectro.covid19india.entity.District
import com.codelectro.covid19india.entity.StateWise

@Database(
    entities = [CasesSeries::class, StateWise::class, District::class],
    version = 1
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun getMainDao() : MainDao
}