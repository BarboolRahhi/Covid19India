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
    fun getDistrictByStateCode(code: String): LiveData<List<District>>

    @Query("SELECT * FROM cases_series")
    suspend fun getAllCasesSeries(): List<CasesSeries>

    @Query("SELECT * FROM state_wise WHERE statecode != 'TT' ORDER BY confirmed DESC")
    fun getAllStateWise(): LiveData<List<StateWise>>

    @Query("SELECT * FROM state_wise WHERE statecode = :code")
    fun getTotalData(code: String): LiveData<StateWise?>

    @Query("Delete FROM cases_series")
    suspend fun deleteAllCasesSeries()

    @Query("Delete FROM state_wise")
    suspend fun deleteAllStateWise()

}