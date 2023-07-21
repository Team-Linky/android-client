package com.linky.data_base.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.linky.model.open_graph.OpenGraphData
import com.squareup.moshi.Moshi

@ProvidedTypeConverter
class OpenGraphDataConverter constructor(
    private val moshi: Moshi
) {

    @TypeConverter
    fun toOpenGraphData(value: String): OpenGraphData? =
        moshi.adapter(OpenGraphData::class.java).fromJson(value)

    @TypeConverter
    fun fromOpenGraphData(data: OpenGraphData): String =
        moshi.adapter(OpenGraphData::class.java).toJson(data)

}