package com.codelectro.covid19india.util

import com.codelectro.covid19india.entity.DistrictData
import com.codelectro.covid19india.entity.States
import com.google.gson.*
import java.lang.reflect.Type

class DataParser : JsonDeserializer<States> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): States? {

        var map: HashMap<String, DistrictData>? = null

        try {
             map = readServiceUrlMap(json?.asJsonObject)
        } catch (e: JsonSyntaxException) {
            return null;
        }

        return map?.let { States(it) }
    }

    private fun readServiceUrlMap(jsonObject: JsonObject?): HashMap<String, DistrictData>? {

        if (jsonObject == null) {
            return null
        }

        val gson = Gson()
        val map = HashMap<String, DistrictData>()

        for (entry in jsonObject.entrySet()) {
            val key = entry.key
            val data = gson.fromJson(entry.value, DistrictData::class.java)
            map[key] = data
        }

        return map
    }
}











