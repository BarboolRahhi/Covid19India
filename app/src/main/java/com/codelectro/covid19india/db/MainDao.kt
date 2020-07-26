package com.codelectro.covid19india.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.codelectro.covid19india.entity.CasesSeries
import com.codelectro.covid19india.entity.District
import com.codelectro.covid19india.entity.StateWise

@Dao
interface MainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCasesSeries(casesSeries: List<CasesSeries>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStateWise(stateWise: List<StateWise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDistricts(districts: List<District>)

    @Query("SELECT * FROM districts WHERE statecode = :code ORDER BY confirmed DESC")
    suspend fun getDistrictByStateCodeAndConfirmed(code: String): List<District>

    @Query("SELECT * FROM districts WHERE statecode = :code ORDER BY active DESC")
    suspend fun getDistrictByStateCodeAndActive(code: String): List<District>

    @Query("SELECT * FROM districts WHERE statecode = :code ORDER BY recovered DESC")
    suspend fun getDistrictByStateCodeAndRecovered(code: String): List<District>

    @Query("SELECT * FROM districts WHERE statecode = :code ORDER BY deceased DESC")
    suspend fun getDistrictByStateCodeAndDeaths(code: String): List<District>

    @Query("SELECT * FROM cases_series")
    suspend fun getAllCasesSeries(): List<CasesSeries>

    @Query("SELECT * FROM state_wise WHERE statecode != 'TT' ORDER BY confirmed DESC")
    suspend fun getAllStateWiseByConfirmed(): List<StateWise>

    @Query("SELECT * FROM state_wise WHERE statecode != 'TT' ORDER BY active DESC")
    suspend fun getAllStateWiseByActive(): List<StateWise>

    @Query("SELECT * FROM state_wise WHERE statecode != 'TT' ORDER BY recovered DESC")
    suspend fun getAllStateWiseByRecovered(): List<StateWise>

    @Query("SELECT * FROM state_wise WHERE statecode != 'TT' ORDER BY deaths DESC")
    suspend fun getAllStateWiseByDeaths(): List<StateWise>

    @Query("SELECT * FROM state_wise WHERE statecode = :code")
    fun getTotalData(code: String): LiveData<StateWise?>

    @Query("Delete FROM cases_series")
    suspend fun deleteAllCasesSeries()

    @Query("Delete FROM state_wise")
    suspend fun deleteAllStateWise()

}