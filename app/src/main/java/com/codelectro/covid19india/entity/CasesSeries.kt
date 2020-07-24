package com.codelectro.covid19india.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases_series")
data class CasesSeries(
    @PrimaryKey
    val date: String,
    val totalconfirmed: Int = 0,
    val totaldeceased: Int = 0,
    val totalrecovered: Int = 0
)