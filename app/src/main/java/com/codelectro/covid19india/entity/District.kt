package com.codelectro.covid19india.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "districts")
data class District(
  @PrimaryKey
  val keyId: String,
  val name: String,
  val statecode: String,
  val active: Int = 0,
  val confirmed: Int = 0,
  val deceased: Int = 0,
  val recovered: Int = 0,
  val deltarecovered: Int = 0,
  val deltaconfirmed: Int = 0,
  val deltadeaths: Int = 0
)