package com.linky.data.repository.backup

interface DatabaseBackupRepository {
    suspend fun backup(
        storageDir: String,
        dataDir: String,
    )
    suspend fun restore()
}