package com.linky.feature.link_modifier.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.linky.design_system.R
import com.linky.design_system.ui.component.chip.LinkyTagChip
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyUrlInputTextField
import com.linky.design_system.ui.theme.ColorFamilyGray100AndGray900
import com.linky.design_system.ui.theme.ColorFamilyGray300AndGray800
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.SubColor
import com.linky.feature.link_modifier.state.OpenGraphStatus
import com.linky.model.Tag
import com.linky.model.TagWithUsage

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LinkModifierContent(
    modifier: Modifier = Modifier,
    tags: LazyPagingItems<TagWithUsage>,
    openGraphStatus: OpenGraphStatus,
    selectTags: List<Tag>,
    memoValue: String,
    memoOnValueChange: (String) -> Unit,
    memoOnClear: () -> Unit,
    memoOnFocusChanged: (FocusState) -> Unit = {},
    memoFocusRequester: FocusRequester,
    tagValue: String,
    tagOnValueChange: (String) -> Unit,
    tagOnClear: () -> Unit,
    tagOnFocusChanged: (FocusState) -> Unit = {},
    tagFocusRequester: FocusRequester,
    focusManager: FocusManager,
    onCreateTag: (String) -> Unit,
    onSelectTag: (Tag) -> Unit,
    onUnSelectTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 32.dp))
            LinkyText(
                text = stringResource(R.string.link_memo),
                fontSize = 12.dp,
                fontWeight = FontWeight.Medium,
                color = ColorFamilyGray800AndGray300
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            LinkyUrlInputTextField(
                value = memoValue,
                placeholder = stringResource(R.string.link_modifier_memo_placeholder),
                onValueChange = memoOnValueChange,
                onClear = memoOnClear,
                onFocusChanged = memoOnFocusChanged,
                focusRequester = memoFocusRequester,
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            Spacer(modifier = Modifier.padding(top = 32.dp))
            LinkyText(
                text = stringResource(R.string.tag_add),
                fontSize = 12.dp,
                fontWeight = FontWeight.Medium,
                color = ColorFamilyGray800AndGray300
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            LinkyUrlInputTextField(
                modifier = Modifier.fillMaxWidth(),
                value = tagValue,
                placeholder = stringResource(R.string.tag_modifier_placeholder),
                onValueChange = tagOnValueChange,
                onClear = tagOnClear,
                onFocusChanged = tagOnFocusChanged,
                focusRequester = tagFocusRequester,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onCreateTag.invoke(tagValue)
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            FlowRow {
                (0..<tags.itemCount).forEach { index ->
                    tags[index]?.let { tagWithUsage ->
                        val tag = tagWithUsage.tag

                        LaunchedEffect(tagWithUsage.tag) {
                            if (tagWithUsage.isUsed) {
                                onSelectTag.invoke(tag)
                            } else {
                                onUnSelectTag.invoke(tag)
                            }
                        }

                        Box(
                            modifier = Modifier.padding(end = 4.dp, bottom = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            LinkyTagChip(
                                text = tag.name,
                                isSelected = selectTags.contains(tag),
                                onDelete = { onDeleteTag.invoke(tag) },
                                onClick = {
                                    if (tag.id != null) {
                                        if (selectTags.contains(tag)) {
                                            onUnSelectTag.invoke(tag)
                                        } else {
                                            onSelectTag.invoke(tag)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 36.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorFamilyGray300AndGray800)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorFamilyGray100AndGray900),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (openGraphStatus) {
                is OpenGraphStatus.Idle -> {
                    Box(modifier = Modifier.padding(8.dp)) {
                        CircularProgressIndicator()
                    }
                }
                is OpenGraphStatus.Error -> {
                    LinkyText(
                        text = stringResource(R.string.open_graph_error),
                        fontSize = 13.dp,
                        fontWeight = FontWeight.Medium,
                        color = ColorFamilyGray800AndGray300,
                    )
                }
                is OpenGraphStatus.Success -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        LinkyText(
                            text = openGraphStatus.openGraphData.siteName ?: "null",
                            color = SubColor,
                            fontSize = 13.dp,
                            fontWeight = FontWeight.SemiBold
                        )
                        LinkyText(
                            text = openGraphStatus.openGraphData.title ?: "null",
                            color = ColorFamilyGray600AndGray400,
                            fontSize = 13.dp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    LinkyText(
                        text = openGraphStatus.openGraphData.url ?: "null",
                        fontSize = 13.dp,
                        fontWeight = FontWeight.Normal,
                        color = ColorFamilyGray800AndGray300,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}