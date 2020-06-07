package com.codelectro.covid19india.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Summary (
	@SerializedName("Active cases") @Expose val activeCases : Int,
	@SerializedName("Cured/Discharged/Migrated") @Expose val cured : Int,
	@SerializedName("Death") @Expose val death : Int,
	@SerializedName("Total Cases") @Expose val totalCases : Int
) {

	override fun toString(): String {
		return "Summary(activeCases=$activeCases, cured=$cured, death=$death, totalCases=$totalCases)"
	}
}