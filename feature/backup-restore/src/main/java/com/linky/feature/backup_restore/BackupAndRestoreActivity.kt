package com.linky.feature.backup_restore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.linky.design_system.R
import com.linky.design_system.animation.slideIn
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray999
import com.linky.design_system.ui.theme.LinkyLinkTheme
import com.linky.feature.backup_restore.state.BackupAndRestoreSideEffect
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectSideEffect

fun ComponentActivity.launchBackupAndRestoreActivity() {
    Intent(this, BackupAndRestoreActivity::class.java).apply {
        startActivity(this)
        slideIn()
    }
}

@AndroidEntryPoint
class BackupAndRestoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkyLinkTheme {
                val scaffoldState = rememberScaffoldState()
                val viewModel = hiltViewModel<BackupAndRestoreViewModel>()

                viewModel.collectSideEffect { sideEffect ->
                    when (sideEffect) {
                        is BackupAndRestoreSideEffect.ShowSnackBar -> {
                            scaffoldState.snackbarHostState.showSnackbar(sideEffect.message)
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    backgroundColor = ColorFamilyWhiteAndGray999
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        BackupAndRestoreHeader { finish() }
                        BackupAndRestoreScreen(
                            onBackup = viewModel::backup
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BackupAndRestoreScreen(
    onBackup: () -> Unit
) {
    LinkyButton(
        text = "백업하기",
        onClick = onBackup
    )
}

@Composable
private fun BackupAndRestoreHeader(
    onBack: () -> Unit,
) {
    LinkyHeader(
        modifier = Modifier.padding(start = 12.dp, end = 16.dp)
    ) {
        LinkyBackArrowButton(onClick = onBack)
        LinkyText(
            text = stringResource(R.string.backup_restore_title),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.dp,
            color = ColorFamilyGray900AndGray100,
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}