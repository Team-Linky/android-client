package com.linky.data.repository.certification

import com.linky.data_store.data_source.certification.CertificationDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CertificationRepositoryImpl @Inject constructor(
    private val certificationDataSource: CertificationDataSource
) : CertificationRepository {

    override suspend fun setPassword(password: String) =
        certificationDataSource.setPassword(password)

    override fun getPassword(): Flow<String?> =
        certificationDataSource.getPassword()

    override fun existCertification(): Flow<Boolean> =
        certificationDataSource.existCertification()

    override fun certified(password: String): Flow<Boolean> =
        certificationDataSource.certified(password)

}