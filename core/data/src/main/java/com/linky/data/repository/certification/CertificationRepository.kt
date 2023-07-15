package com.linky.data.repository.certification

import kotlinx.coroutines.flow.Flow

interface CertificationRepository {
    suspend fun setPassword(password: String)
    fun getPassword(): Flow<String?>
    fun existCertification(): Flow<Boolean>
    fun certified(password: String): Flow<Boolean>
}