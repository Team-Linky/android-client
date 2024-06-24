package com.linky.link_detail_input.component

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
import com.linky.model.Tag

internal data class TagDeleteDialogDirector(
    val tag: Tag?,
    val isShow: Boolean
) {
    companion object {
        val Init get() = TagDeleteDialogDirector(tag = null, isShow = false)

        fun Tag.of(isShow: Boolean = true) = TagDeleteDialogDirector(tag = this, isShow = isShow)
    }
}

@Composable
internal fun TagDeleteDialog(
    director: TagDeleteDialogDirector,
    onCancel: () -> Unit,
    onDelete: (Tag) -> Unit,
) {
    if (director.isShow) {
        AlertDialog(
            shape = RoundedCornerShape(14.dp),
            backgroundColor = ColorFamilyWhiteAndGray800,
            title = {
                LinkyText(
                    text = String.format(stringResource(R.string.tag_delete_confirm_title), director.tag!!.name),
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                LinkyText(
                    text = stringResource(R.string.tag_delete_confirm_desc),
                    fontWeight = FontWeight.Medium
                )
            },
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(onClick = { onDelete.invoke(director.tag!!) }) {
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