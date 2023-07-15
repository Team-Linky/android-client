package com.linky.data_store.data_source.certification

import kotlinx.coroutines.flow.Flow

interface CertificationDataSource {
    suspend fun setPassword(password: String)
    fun getPassword(): Flow<String?>
    fun existCertification(): Flow<Boolean>
    fun certified(password: String): Flow<Boolean>
}