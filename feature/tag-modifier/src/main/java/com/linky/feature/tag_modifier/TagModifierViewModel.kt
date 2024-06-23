package com.linky.feature.tag_modifier

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.linky.data.usecase.tag.SelectAllWithUsageUseCase
import com.linky.data.usecase.tag.TagInsertUseCase
import com.linky.feature.tag_modifier.state.Mode
import com.linky.feature.tag_modifier.state.TagModifierSideEffect
import com.linky.feature.tag_modifier.state.TagModifierState
import com.linky.feature.tag_modifier.state.TagModifierState.Companion.Init
import com.linky.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TagModifierViewModel @Inject constructor(
    private val selectAllWithUsageUseCase: SelectAllWithUsageUseCase,
    private val tagInsertUseCase: TagInsertUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ContainerHost<TagModifierState, TagModifierSideEffect>, ViewModel() {

    override val container = container<TagModifierState, TagModifierSideEffect>(Init)

    fun doAction(action: Action) {
        when (action) {
            is Action.InsertTag -> insertTag(action.tagName)
        }
    }

    private fun insertTag(tagName: String) {
        intent {
            tagInsertUseCase.invoke(Tag(name = tagName))
        }
    }

    private fun setMode() {
        intent {
            savedStateHandle.get<Int>("mode")?.let { id ->
                val mode = when (id) {
                    1 -> Mode.Creator
                    2 -> Mode.Editor
                    else -> Mode.None
                }
                reduce { state.copy(mode = mode) }
            }
        }
    }

    private fun getTags() {
        intent {
            savedStateHandle.get<Long>("linkId")
                ?.let { selectAllWithUsageUseCase.invoke(it).cachedIn(viewModelScope) }
                ?.also { reduce { state.copy(tags = it) } }
        }
    }

    init {
        setMode()
        getTags()
    }

}

sealed interface Action {
    data class InsertTag(val tagName: String) : Action
}