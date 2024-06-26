package com.linky.feature.recycle_bin.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray800
import com.linky.design_system.ui.theme.ErrorColor

internal data class LinkDeleteDialogDirector(
    val isShow: Boolean
) {
    companion object {
        val Init get() = LinkDeleteDialogDirector(isShow = false)
    }
}

@Composable
internal fun DeleteLinkConfirmDialog(
    director: LinkDeleteDialogDirector,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    if (director.isShow) {
        AlertDialog(
            shape = RoundedCornerShape(14.dp),
            backgroundColor = ColorFamilyWhiteAndGray800,
            title = {
                LinkyText(
                    text = stringResource(R.string.recycle_bin_confirm_title_clear),
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                LinkyText(
                    text = stringResource(R.string.recycle_bin_confirm_desc_clear),
                    fontWeight = FontWeight.Medium
                )
            },
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(onClick = onDelete) {
                    LinkyText(
                        text = stringResource(R.string.delete),
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorColor,
                        fontSize = 14.sp
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    LinkyText(
                        text = stringResource(R.string.cancel),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        )
    }
}
