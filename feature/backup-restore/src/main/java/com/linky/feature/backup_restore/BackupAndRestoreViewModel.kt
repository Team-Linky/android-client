package com.linky.feature.backup_restore

import android.os.Environment
import androidx.lifecycle.ViewModel
import com.linky.data.usecase.backup.DataBaseBackupUseCase
import com.linky.feature.backup_restore.state.BackupAndRestoreSideEffect
import com.linky.feature.backup_restore.state.BackupAndRestoreState
import com.linky.feature.backup_restore.state.BackupAndRestoreState.Companion.Init
import com.linky.feature.backup_restore.state.BackupStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BackupAndRestoreViewModel @Inject constructor(
    private val dataBaseBackupUseCase: DataBaseBackupUseCase
) : ContainerHost<BackupAndRestoreState, BackupAndRestoreSideEffect>, ViewModel() {

    override val container = container<BackupAndRestoreState, BackupAndRestoreSideEffect>(Init)

    fun backup() {
        intent {
            try {
                reduce { state.copy(backupStatus = BackupStatus.Loading) }

                val storageDir = Environment.getExternalStorageDirectory().path
                val dataDir = Environment.getDataDirectory().path

                dataBaseBackupUseCase.invoke(storageDir, dataDir)

                reduce { state.copy(backupStatus = BackupStatus.Success) }
            } catch (e: Exception) {
                reduce { state.copy(backupStatus = BackupStatus.UnInit) }
                postSideEffect(BackupAndRestoreSideEffect.ShowSnackBar("알 수 없는 문제가 발생했습니다."))
            }
        }
    }

}