package com.linky.feature.backup_restore.state

data class BackupAndRestoreState(
    val backupStatus: BackupStatus
) {
    companion object {
        val Init = BackupAndRestoreState(backupStatus = BackupStatus.UnInit)
    }
}

sealed interface BackupStatus {
    data object UnInit : BackupStatus
    data object Loading : BackupStatus
    data object Success : BackupStatus
}