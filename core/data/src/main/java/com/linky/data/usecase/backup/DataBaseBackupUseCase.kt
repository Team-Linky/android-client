package com.linky.data.usecase.backup

import com.linky.data.repository.backup.DatabaseBackupRepository
import javax.inject.Inject

class DataBaseBackupUseCase @Inject constructor(
    private val databaseBackupRepository: DatabaseBackupRepository
) {
    suspend operator fun invoke(
        storageDir: String,
        dataDir: String
    ) = databaseBackupRepository.backup(
        storageDir = storageDir,
        dataDir = dataDir
    )
}