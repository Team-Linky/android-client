package com.linky.data_store.data_source.certification

import kotlinx.coroutines.flow.Flow

interface PinDataSource {
    suspend fun setPin(password: String)
    val pin: Flow<String?>
    val existPin: Flow<Boolean>
    fun certified(pin: String): Flow<Boolean>
}