package com.codelectro.covid19india.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state_wise")
data class StateWise(
    @PrimaryKey
    val statecode: String,
    val state: String,
    val active: Int = 0,
    val confirmed: Int = 0,
    val deaths: Int = 0,
    val recovered: Int = 0,
    val lastupdatedtime: String? = null
)