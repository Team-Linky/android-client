package com.linky.data.repository.backup

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

class DatabaseBackupRepositoryImpl @Inject constructor() : DatabaseBackupRepository {

    override suspend fun backup(
        storageDir: String,
        dataDir: String,
    ): Unit = coroutineScope {
        val backupPaths = listOf(
            FROM_PATH_DB to TO_PATH_DB,
            FROM_PATH_SHM to TO_PATH_SHM,
            FROM_PATH_WAL to TO_PATH_WAL
        )

        backupPaths.map { (from, to) ->
            async(Dispatchers.IO) {
                createBackupFile(
                    storageDir = storageDir,
                    dataDir = dataDir,
                    from = from,
                    to = to
                )
            }
        }.awaitAll()
    }

    override suspend fun restore() {

    }

    private fun createBackupFile(
        storageDir: String,
        dataDir: String,
        from: String,
        to: String
    ) {
        val currentDB = File(dataDir, from)
        val backupDB = File(storageDir, to)

        val src = FileInputStream(currentDB).channel
        val dst = FileOutputStream(backupDB).channel

        dst.use {
            src.use {
                dst.transferFrom(src, 0, src.size())
            }
        }
    }

    companion object {
        private const val FROM_PATH_DB = "/data/com.linky.android/databases/linky_db"
        private const val FROM_PATH_SHM = "/data/com.linky.android/databases/linky_db-shm"
        private const val FROM_PATH_WAL = "/data/com.linky.android/databases/linky_db-wal"

        private const val TO_PATH_DB = "/Download/linky_db"
        private const val TO_PATH_SHM = "/Download/linky_db-shm"
        private const val TO_PATH_WAL = "/Download/linky_db-wal"
    }

}