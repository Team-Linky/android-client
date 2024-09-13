package com.linky.feature.backup_restore.state

sealed interface BackupAndRestoreSideEffect {
    data class ShowSnackBar(val message: String) : BackupAndRestoreSideEffect
}