package com.codelectro.covid19india.entity


data class DistrictNetwork(
  val active: Int = 0,
  val confirmed: Int = 0,
  val deceased: Int = 0,
  val recovered: Int = 0
)