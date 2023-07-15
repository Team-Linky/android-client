package com.linky.data.repository.lock

import kotlinx.coroutines.flow.Flow

interface LockRepository {

    /** 화면 잠금 설정 */
    suspend fun enableLock(enable: Boolean)
    fun isEnabled(): Flow<Boolean>

    /** 생체 인식 설정 */
    suspend fun enableBiometric(enable: Boolean)
    fun isEnabledBiometric(): Flow<Boolean>
}