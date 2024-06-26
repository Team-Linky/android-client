package com.linky.feature.tag_setting.modifier

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.data.usecase.tag.TagInsertUseCase
import com.linky.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TagModifierViewModel @Inject constructor(
    private val tagInsertUseCase: TagInsertUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val tag get() = savedStateHandle.get<Tag>("tag")

    val mode get() = if (tag == null) {
        ModifierMode.Add
    } else {
        ModifierMode.Edit
    }

    fun insert(
        tag: Tag,
        success: () -> Unit,
        error: () -> Unit
    ) {
        viewModelScope.safeLaunch {
            try {
                val id = tagInsertUseCase.invoke(tag)

                if (id > -1) {
                    success.invoke()
                }
            } catch (e: Exception) {
                error.invoke()
            }
        }
    }

}