package com.linky.data.repository.pin

import kotlinx.coroutines.flow.Flow

interface PinRepository {
    suspend fun setPin(password: String)
    val pin: Flow<String?>
    val existPin: Flow<Boolean>
    fun certified(pin: String): Flow<Boolean>
}