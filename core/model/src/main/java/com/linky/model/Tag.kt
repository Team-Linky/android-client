package com.linky.model

data class Tag(
    val id: Long? = null,
    val name: String,
    val linkIds: List<Long> = emptyList()
)