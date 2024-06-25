package com.linky.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Random

@Parcelize
data class Tag(
    val id: Long? = null,
    val name: String,
    val linkIds: List<Long> = emptyList(),
    val red: Float = Random().nextFloat() * (0.9f - 0.6f) + 0.6f,
    val green: Float = Random().nextFloat() * (0.9f - 0.6f) + 0.6f,
    val blue: Float = Random().nextFloat() * (0.9f - 0.6f) + 0.6f,
) : Parcelable