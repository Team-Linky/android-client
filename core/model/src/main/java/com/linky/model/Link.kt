package com.linky.model

import com.linky.model.open_graph.OpenGraphData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Link(
    val id: Long? = null,
    val memo: String,
    val openGraphData: OpenGraphData,
    val tags: List<Long> = emptyList(),
    val readCount: Long = 0,
    val createAt: Long = System.currentTimeMillis(),
    val isRemove: Boolean = false,
    val tagList: List<Tag> = emptyList()
) {
    val createAtFormat: String
        get() {
            val date = Date(createAt)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getDefault()

            return simpleDateFormat.format(date)
        }

    val readCountFormat: String get() = if (readCount == 0L) "안읽음" else "${readCount}번 읽음"
}